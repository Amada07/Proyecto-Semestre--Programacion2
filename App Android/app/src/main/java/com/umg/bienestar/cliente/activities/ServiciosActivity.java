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
import com.umg.bienestar.cliente.adapters.ServiciosAdapter;
import com.umg.bienestar.cliente.api.ApiClient;
import com.umg.bienestar.cliente.api.ApiService;
import com.umg.bienestar.cliente.models.Servicio;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiciosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ServiciosAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SwipeRefreshLayout swipeRefresh;
    private ApiService apiService;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        // Configurar ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Servicios Disponibles");
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewServicios);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiciosAdapter(this);
        recyclerView.setAdapter(adapter);

        // Inicializar servicios
        apiService = ApiClient.getApiService(this);
        prefsManager = SharedPrefsManager.getInstance(this);

        // SwipeRefresh
        swipeRefresh.setOnRefreshListener(() -> cargarServicios());

        // Cargar servicios
        cargarServicios();
    }

    private void cargarServicios() {
        showLoading(true);
        tvEmpty.setVisibility(View.GONE);

        String token = "Bearer " + prefsManager.getToken();

        apiService.obtenerServiciosActivos(token).enqueue(new Callback<List<Servicio>>() {
            @Override
            public void onResponse(Call<List<Servicio>> call, Response<List<Servicio>> response) {
                showLoading(false);
                swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Servicio> servicios = response.body();

                    if (servicios.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        adapter.setServicios(servicios);
                    }
                } else {
                    Toast.makeText(ServiciosActivity.this,
                            "Error al cargar servicios",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Servicio>> call, Throwable t) {
                showLoading(false);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(ServiciosActivity.this,
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}