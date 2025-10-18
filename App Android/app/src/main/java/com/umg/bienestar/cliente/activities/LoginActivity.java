package com.umg.bienestar.cliente.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.umg.bienestar.cliente.R;
import com.umg.bienestar.cliente.api.ApiClient;
import com.umg.bienestar.cliente.api.ApiService;
import com.umg.bienestar.cliente.models.LoginRequest;
import com.umg.bienestar.cliente.models.LoginResponse;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private ApiService apiService;
    private SharedPrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        // Inicializar servicios
        apiService = ApiClient.getApiService(this);
        prefsManager = SharedPrefsManager.getInstance(this);

        // Verificar si ya hay sesión activa
        if (prefsManager.isLoggedIn()) {
            navigateToMain();
            return;
        }

        // Click listener del botón login
        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validaciones
        if (username.isEmpty()) {
            etUsername.setError(getString(R.string.error_empty_username));
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError(getString(R.string.error_empty_password));
            etPassword.requestFocus();
            return;
        }

        // Mostrar loading
        showLoading(true);

        // Crear request
        LoginRequest request = new LoginRequest(username, password);

        // Llamar API
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                     //Agregamos estos logs
                    android.util.Log.d("LOGIN_DEBUG", "===== DATOS DE LOGIN =====");
                    android.util.Log.d("LOGIN_DEBUG", "Token: " + loginResponse.getToken());
                    android.util.Log.d("LOGIN_DEBUG", "Username: " + loginResponse.getUsername());
                    android.util.Log.d("LOGIN_DEBUG", "Rol: " + loginResponse.getRol());
                    android.util.Log.d("LOGIN_DEBUG", "ID: " + loginResponse.getId());
                    android.util.Log.d("LOGIN_DEBUG", "==========================");

                    // Guardar datos en SharedPreferences
                    prefsManager.saveToken(loginResponse.getToken());
                    prefsManager.saveUserData(
                            loginResponse.getId(),
                            loginResponse.getUsername(),
                            loginResponse.getRol(),
                            "" // email (opcional)
                    );

                    // Navegar a MainActivity
                    navigateToMain();

                    Toast.makeText(LoginActivity.this,
                            "Bienvenido " + loginResponse.getUsername(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.error_login_failed),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(LoginActivity.this,
                        getString(R.string.error_network) + ": " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        etUsername.setEnabled(!show);
        etPassword.setEnabled(!show);
    }
}