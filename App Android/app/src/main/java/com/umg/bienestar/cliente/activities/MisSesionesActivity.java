package com.umg.bienestar.cliente.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.adapters.CitasAdapter;
import com.umg.bienestar.cliente.api.ApiClient;
import com.umg.bienestar.cliente.api.ApiService;
import com.umg.bienestar.cliente.models.Cita;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisSesionesActivity extends AppCompatActivity implements CitasAdapter.OnCitaCanceladaListener {

    private RecyclerView recyclerView;
    private CitasAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SwipeRefreshLayout swipeRefresh;
    private ApiService apiService;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_sesiones);

        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mis Citas");
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewCitas);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CitasAdapter(this, this);
        recyclerView.setAdapter(adapter);

        // Inicializar servicios
        apiService = ApiClient.getApiService(this);
        prefsManager = SharedPrefsManager.getInstance(this);

        // SwipeRefresh
        swipeRefresh.setOnRefreshListener(() -> cargarCitas());

        // Cargar citas
        cargarCitas();
    }

    private void cargarCitas() {
        showLoading(true);
        tvEmpty.setVisibility(View.GONE);

        String token = "Bearer " + prefsManager.getToken();
        Long clienteId = prefsManager.getUserId();

        apiService.obtenerCitasCliente(token, clienteId).enqueue(new Callback<List<Cita>>() {
            @Override
            public void onResponse(Call<List<Cita>> call, Response<List<Cita>> response) {
                showLoading(false);
                swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Cita> citas = response.body();

                    if (citas.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        adapter.setCitas(citas);
                    }
                } else {
                    Toast.makeText(MisSesionesActivity.this,
                            "Error al cargar citas",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Cita>> call, Throwable t) {
                showLoading(false);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(MisSesionesActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onCitaCancelada() {
        // Recargar lista cuando se cancela una cita
        cargarCitas();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}