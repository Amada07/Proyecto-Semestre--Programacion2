package com.umg.bienestar.cliente.utils;

import android.content.Context;
import android.content.SharedPreferences;
public class SharedPrefsManager {
    private static SharedPrefsManager instance;
    private SharedPreferences prefs;

    private SharedPrefsManager(Context context) {
        prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsManager(context.getApplicationContext());
        }
        return instance;
    }

    // Guardar token
    public void saveToken(String token) {
        prefs.edit().putString(Constants.KEY_TOKEN, token).apply();
    }

    // Obtener token
    public String getToken() {
        return prefs.getString(Constants.KEY_TOKEN, null);
    }

    // Guardar datos de usuario
    public void saveUserData(Long userId, String username, String role, String email) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_USERNAME, username);
        editor.putString(Constants.KEY_USER_ROLE, role);
        editor.putString(Constants.KEY_USER_EMAIL, email);
        editor.apply();
    }


    // Obtener ID de usuario
    public Long getUserId() {
        long id = prefs.getLong(Constants.KEY_USER_ID, -1);
        return id != -1 ? id : null;
    }

    // Obtener username
    public String getUsername() {
        return prefs.getString(Constants.KEY_USERNAME, "");
    }

    // Verificar si hay sesión activa
    public boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    // Cerrar sesión
    public void logout() {
        prefs.edit().clear().apply();
    }
}
