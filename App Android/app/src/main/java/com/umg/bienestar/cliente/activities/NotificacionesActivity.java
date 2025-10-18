package com.umg.bienestar.cliente.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.adapters.NotificacionesAdapter;
import com.umg.bienestar.cliente.models.Notificacion;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificacionesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificacionesAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SwipeRefreshLayout swipeRefresh;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notificaciones");
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewNotificaciones);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificacionesAdapter(this);
        recyclerView.setAdapter(adapter);

        // Inicializar servicios
        prefsManager = SharedPrefsManager.getInstance(this);

        // SwipeRefresh
        swipeRefresh.setOnRefreshListener(() -> cargarNotificaciones());

        // Cargar notificaciones
        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        showLoading(true);
        tvEmpty.setVisibility(View.GONE);

        // SIMULACIÓN DE NOTIFICACIONES
        // En una implementación real, esto vendría del backend
        List<Notificacion> notificacionesSimuladas = generarNotificacionesSimuladas();

        // Simular delay de red
        recyclerView.postDelayed(() -> {
            showLoading(false);
            swipeRefresh.setRefreshing(false);

            if (notificacionesSimuladas.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
            } else {
                adapter.setNotificaciones(notificacionesSimuladas);
            }
        }, 1000);
    }

    private List<Notificacion> generarNotificacionesSimuladas() {
        List<Notificacion> notificaciones = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        // Notificación 1: Cita confirmada (hace 2 horas)
        Date fecha1 = new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000));
        Notificacion n1 = new Notificacion(
                "CITA_CONFIRMADA",
                "Cita Confirmada",
                "Tu cita para Masaje Relajante el 20 Oct a las 14:00 ha sido confirmada",
                sdf.format(fecha1)
        );
        n1.setLeida(false);
        notificaciones.add(n1);

        // Notificación 2: Recordatorio (hace 1 día)
        Date fecha2 = new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        Notificacion n2 = new Notificacion(
                "RECORDATORIO",
                "Recordatorio de Cita",
                "Recuerda tu cita de mañana a las 14:00 para Masaje Relajante",
                sdf.format(fecha2)
        );
        n2.setLeida(true);
        notificaciones.add(n2);

        // Notificación 3: Factura generada (hace 3 días)
        Date fecha3 = new Date(System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000));
        Notificacion n3 = new Notificacion(
                "FACTURA",
                "Factura Disponible",
                "Tu factura #001 por Q 150.00 está disponible para consulta",
                sdf.format(fecha3)
        );
        n3.setLeida(true);
        notificaciones.add(n3);

        // Notificación 4: Cita rechazada (hace 5 días)
        Date fecha4 = new Date(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000));
        Notificacion n4 = new Notificacion(
                "CITA_RECHAZADA",
                "Cita No Disponible",
                "Lo sentimos, el horario solicitado para Yoga no está disponible. Por favor elige otro horario",
                sdf.format(fecha4)
        );
        n4.setLeida(true);
        notificaciones.add(n4);

        return notificaciones;
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}