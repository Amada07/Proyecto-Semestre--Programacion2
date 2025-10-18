package com.umg.bienestar.cliente.api;

import com.umg.bienestar.cliente.models.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
public interface ApiService {
    // ============================================
    // AUTENTICACIÓN (UC-M01)
    // ============================================
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // PERFIL CLIENTE (UC-M02)
    @GET("api/clientes/{id}")
    Call<Cliente> obtenerPerfilCliente(
            @Header("Authorization") String token,
            @Path("id") Long clienteId
    );

    // ============================================
    // SERVICIOS (UC-M03)
    // ============================================
    @GET("api/servicios")
    Call<List<Servicio>> obtenerServicios(@Header("Authorization") String token);

    @GET("api/servicios/activos")
    Call<List<Servicio>> obtenerServiciosActivos(@Header("Authorization") String token);

    @GET("api/servicios/{id}")
    Call<Servicio> obtenerServicio(
            @Header("Authorization") String token,
            @Path("id") Long id
    );

    // ============================================
    // CITAS (UC-M04, UC-M05)
    // ============================================
    @POST("api/citas")
    Call<Cita> agendarCita(
            @Header("Authorization") String token,
            @Body CitaRequest request
    );

    @GET("api/citas/cliente/{clienteId}")
    Call<List<Cita>> obtenerCitasCliente(
            @Header("Authorization") String token,
            @Path("clienteId") Long clienteId
    );

    @PUT("api/citas/{id}/cancelar")
    Call<Void> cancelarCita(
            @Header("Authorization") String token,
            @Path("id") Long citaId
    );

    // ============================================
    // FACTURAS (UC-M06)
    // ============================================
    @GET("api/facturas/cliente/{clienteId}")
    Call<List<Factura>> obtenerFacturasCliente(
            @Header("Authorization") String token,
            @Path("clienteId") Long clienteId
    );

    @GET("api/facturas/{id}")
    Call<Factura> obtenerFactura(
            @Header("Authorization") String token,
            @Path("id") Long facturaId
    );
}
