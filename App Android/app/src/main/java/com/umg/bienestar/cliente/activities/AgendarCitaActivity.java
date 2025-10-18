package com.umg.bienestar.cliente.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.api.ApiClient;
import com.umg.bienestar.cliente.api.ApiService;
import com.umg.bienestar.cliente.models.Cita;
import com.umg.bienestar.cliente.models.CitaRequest;
import com.umg.bienestar.cliente.models.Servicio;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendarCitaActivity extends AppCompatActivity {

    private Spinner spinnerServicio;
    private MaterialButton btnSeleccionarFecha, btnSeleccionarHora, btnConfirmar;
    private TextInputEditText etObservaciones;
    private ProgressBar progressBar;

    private ApiService apiService;
    private SharedPrefsManager prefsManager;

    private List<Servicio> servicios;
    private Long servicioSeleccionadoId;
    private String fechaSeleccionada = "";
    private String horaSeleccionada = "";
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Agendar Cita");
        }

        // Inicializar vistas
        spinnerServicio = findViewById(R.id.spinnerServicio);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        etObservaciones = findViewById(R.id.etObservaciones);
        progressBar = findViewById(R.id.progressBar);

        // Inicializar servicios
        apiService = ApiClient.getApiService(this);
        prefsManager = SharedPrefsManager.getInstance(this);
        calendar = Calendar.getInstance();
        servicios = new ArrayList<>();

        // Click listeners
        btnSeleccionarFecha.setOnClickListener(v -> mostrarDatePicker());
        btnSeleccionarHora.setOnClickListener(v -> mostrarTimePicker());
        btnConfirmar.setOnClickListener(v -> agendarCita());

        // Cargar servicios
        cargarServicios();
    }

    private void cargarServicios() {
        String token = "Bearer " + prefsManager.getToken();

        apiService.obtenerServiciosActivos(token).enqueue(new Callback<List<Servicio>>() {
            @Override
            public void onResponse(Call<List<Servicio>> call, Response<List<Servicio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    servicios = response.body();

                    // Crear lista de nombres para el Spinner
                    List<String> nombresServicios = new ArrayList<>();
                    for (Servicio s : servicios) {
                        nombresServicios.add(s.getNombre());
                    }

                    // Configurar Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AgendarCitaActivity.this,
                            android.R.layout.simple_spinner_item,
                            nombresServicios
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerServicio.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Servicio>> call, Throwable t) {
                Toast.makeText(AgendarCitaActivity.this,
                        "Error al cargar servicios",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    fechaSeleccionada = sdf.format(calendar.getTime());

                    btnSeleccionarFecha.setText("Fecha: " + selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                },
                year, month, day
        );

        // No permitir fechas pasadas
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void mostrarTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);

                    horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d:00", selectedHour, selectedMinute);

                    btnSeleccionarHora.setText(String.format(Locale.getDefault(), "Hora: %02d:%02d", selectedHour, selectedMinute));
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    private void agendarCita() {
        // Validaciones
        if (spinnerServicio.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Seleccione un servicio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fechaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Seleccione una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        if (horaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Seleccione una hora", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener servicio seleccionado
        servicioSeleccionadoId = servicios.get(spinnerServicio.getSelectedItemPosition()).getId();

        // Crear request
        String fechaHora = fechaSeleccionada + "T" + horaSeleccionada;
        String observaciones = etObservaciones.getText().toString().trim();
        Long clienteId = prefsManager.getUserId();

        CitaRequest request = new CitaRequest(servicioSeleccionadoId, clienteId, fechaHora, observaciones);

        // Mostrar loading
        showLoading(true);

        // Llamar API
        String token = "Bearer " + prefsManager.getToken();
        apiService.agendarCita(token, request).enqueue(new Callback<Cita>() {
            @Override
            public void onResponse(Call<Cita> call, Response<Cita> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AgendarCitaActivity.this,
                            getString(R.string.cita_agendada_exito),
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(AgendarCitaActivity.this,
                            "Error al agendar cita",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cita> call, Throwable t) {
                showLoading(false);
                Toast.makeText(AgendarCitaActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnConfirmar.setEnabled(!show);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}