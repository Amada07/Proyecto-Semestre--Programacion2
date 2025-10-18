package com.umg.bienestar.cliente.api;

import android.content.Context;
import com.umg.bienestar.cliente.utils.SharedPrefsManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Obtener token de SharedPreferences
        String token = SharedPrefsManager.getInstance(context).getToken();

        Request.Builder builder = originalRequest.newBuilder();

        // Si hay token, agregarlo al header
        if (token != null && !token.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
