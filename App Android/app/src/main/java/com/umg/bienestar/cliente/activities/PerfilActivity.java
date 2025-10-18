package com.umg.bienestar.cliente.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.api.ApiClient;
import com.umg.bienestar.cliente.api.ApiService;
import com.umg.bienestar.cliente.models.Cliente;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvNombreCompleto, tvEmail, tvTelefono, tvDpi, tvDireccion, tvFechaNacimiento;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mi Perfil");
        }

        // Inicializar vistas
        tvNombreCompleto = findViewById(R.id.tvNombreCompleto);
        tvEmail = findViewById(R.id.tvEmail);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvDpi = findViewById(R.id.tvDpi);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento);
        progressBar = findViewById(R.id.progressBar);

        // Inicializar servicios
        apiService = ApiClient.getApiService(this);
        prefsManager = SharedPrefsManager.getInstance(this);

        // Cargar perfil
        cargarPerfil();
    }

    private void cargarPerfil() {
        showLoading(true);

        String token = "Bearer " + prefsManager.getToken();
        Long clienteId = prefsManager.getUserId();

        // AGREGAMOS ESTOS LOGS
        android.util.Log.d("PERFIL_DEBUG", "===== CARGAR PERFIL =====");
        android.util.Log.d("PERFIL_DEBUG", "Token: " + token);
        android.util.Log.d("PERFIL_DEBUG", "Cliente ID: " + clienteId);
        android.util.Log.d("PERFIL_DEBUG", "=========================");

        if (clienteId == null || clienteId == -1) {
            showLoading(false);
            Toast.makeText(this, "Error: No se encontró el ID del usuario. Vuelve a iniciar sesión.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        apiService.obtenerPerfilCliente(token, clienteId).enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                showLoading(false);

                android.util.Log.d("PERFIL_DEBUG", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Cliente cliente = response.body();
                    android.util.Log.d("PERFIL_DEBUG", "Cliente: " + cliente.getNombreCompleto());
                    mostrarDatos(cliente);
                } else {
                    Toast.makeText(PerfilActivity.this,
                            "Error al cargar perfil",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                showLoading(false);
                android.util.Log.e("PERFIL_DEBUG", "Error: " + t.getMessage());
                Toast.makeText(PerfilActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos(Cliente cliente) {
        tvNombreCompleto.setText(cliente.getNombreCompleto());
        tvEmail.setText(cliente.getEmail());
        tvTelefono.setText(cliente.getTelefono() != null ? cliente.getTelefono() : "No especificado");
        tvDpi.setText(cliente.getDpi() != null ? cliente.getDpi() : "No especificado");
        tvDireccion.setText(cliente.getDireccion() != null ? cliente.getDireccion() : "No especificada");

        // Formatear fecha de nacimiento
        if (cliente.getFechaNacimiento() != null) {
            String fechaFormateada = formatearFecha(cliente.getFechaNacimiento());
            tvFechaNacimiento.setText(fechaFormateada);
        } else {
            tvFechaNacimiento.setText("No especificada");
        }
    }

    private String formatearFecha(String fecha) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(fecha);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return fecha;
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}