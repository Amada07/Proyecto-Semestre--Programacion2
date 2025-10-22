/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.dto;

import com.umg.bienestar.sesiones_bienestar.entity.EstadoCita;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author amada
 */
public class CitaDTO {
    private Long id;
    
    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;
    
    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;
    
    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La cita debe ser en el futuro")
    private LocalDateTime fechaHora;
    
    private EstadoCita estado;
    private String notas;
    private String motivoRechazo;
    
    // Campos adicionales para el frontend
    private String clienteNombre;
    private String clienteEmail;
    private String clienteTelefono;
    private String servicioNombre;
    private BigDecimal servicioPrecio;
    private Integer servicioDuracion;
    
    private LocalDateTime fechaCancelacion;
    private LocalDateTime fechaConfirmacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public CitaDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public String getServicioNombre() {
        return servicioNombre;
    }

    public void setServicioNombre(String servicioNombre) {
        this.servicioNombre = servicioNombre;
    }

    public BigDecimal getServicioPrecio() {
        return servicioPrecio;
    }

    public void setServicioPrecio(BigDecimal servicioPrecio) {
        this.servicioPrecio = servicioPrecio;
    }

    public Integer getServicioDuracion() {
        return servicioDuracion;
    }

    public void setServicioDuracion(Integer servicioDuracion) {
        this.servicioDuracion = servicioDuracion;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public LocalDateTime getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}