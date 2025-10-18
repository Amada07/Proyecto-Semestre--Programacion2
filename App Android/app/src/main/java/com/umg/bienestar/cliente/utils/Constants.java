package com.umg.bienestar.cliente.utils;

public class Constants {
    // URL del backend - AJUSTAR según tu configuración
    // OPCIÓN 1: Emulador de Android Studio (RECOMENDADO PARA DESARROLLO)
    // 10.0.2.2 es cómo el emulador accede a localhost en tu máquina
    public static final String BASE_URL = "http://10.0.2.2:8080/";

    // OPCIÓN 2: Dispositivo físico (descomenta y reemplaza la IP)
    // Para usar, comenta la OPCIÓN 1 y descomenta esto
    // Reemplaza 192.168.X.X con tu IP local donde corre el backend
    // Ejemplo: si tu computadora tiene IP 192.168.1.105, usa "http://192.168.1.71 :8080/"
    // public static final String BASE_URL = "http://192.168.1.X:8080/";

    // OPCIÓN 3: Servidor en la nube (producción)
    // Cuando tu backend esté en un servidor público
    // public static final String BASE_URL = "https://tudominio.com/";

    // SharedPreferences
    public static final String PREFS_NAME = "BienestarPrefs";
    public static final String KEY_TOKEN = "jwt_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USER_ROLE = "user_role";
    public static final String KEY_USER_EMAIL = "user_email";
}
