package com.umg.bienestar.cliente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.button.MaterialButton;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;

public class MainActivity extends AppCompatActivity {

    private TextView tvUsername;
    private CardView cardServicios, cardAgendar, cardMisCitas, cardFacturas, cardPerfil, cardNotificaciones;
    private MaterialButton btnLogout;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar SharedPrefsManager
        prefsManager = SharedPrefsManager.getInstance(this);

        // Inicializar vistas
        tvUsername = findViewById(R.id.tvUsername);
        cardServicios = findViewById(R.id.cardServicios);
        cardAgendar = findViewById(R.id.cardAgendar);
        cardMisCitas = findViewById(R.id.cardMisCitas);
        cardFacturas = findViewById(R.id.cardFacturas);
        cardPerfil = findViewById(R.id.cardPerfil);
        cardNotificaciones = findViewById(R.id.cardNotificaciones);
        btnLogout = findViewById(R.id.btnLogout);

        // Mostrar nombre de usuario
        String username = prefsManager.getUsername();
        tvUsername.setText("Hola, " + username);

        // Click listeners
        cardServicios.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ServiciosActivity.class);
            startActivity(intent);
        });

        cardAgendar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgendarCitaActivity.class);
            startActivity(intent);
        });

        cardMisCitas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MisSesionesActivity.class);
            startActivity(intent);
        });

        cardFacturas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MisFacturasActivity.class);
            startActivity(intent);
        });

        //  Click listener para Mi Perfil
        cardPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        //Para Notificaciones
        cardNotificaciones.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotificacionesActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> showLogoutDialog());

        // Manejar botón atrás con OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Evitar que el usuario regrese al login
                moveTaskToBack(true);
            }
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.menu_logout))
                .setMessage(getString(R.string.confirm_logout))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> logout())
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    private void logout() {
        prefsManager.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}