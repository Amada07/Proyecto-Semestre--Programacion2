/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author amada
 */
public class SesionHistorialDTO {
    
// Datos de la sesión
    private Long id;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean asistio;
    private String observaciones;
    
    // Datos del cliente
    private Long clienteId;
    private String clienteNombre;
    private String clienteEmail;
    private String clienteTelefono;
    
    // Datos del servicio
    private Long servicioId;
    private String servicioNombre;
    private BigDecimal servicioPrecio;
    private Integer servicioDuracion;
    
    // Datos de la cita asociada
    private Long citaId;
    private LocalDateTime citaFechaHora;
    private String citaEstado;
    
    // Constructor vacío
    public SesionHistorialDTO() {}
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public Boolean getAsistio() {
        return asistio;
    }
    
    public void setAsistio(Boolean asistio) {
        this.asistio = asistio;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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
    
    public Long getServicioId() {
        return servicioId;
    }
    
    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
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
    
    public Long getCitaId() {
        return citaId;
    }
    
    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }
    
    public LocalDateTime getCitaFechaHora() {
        return citaFechaHora;
    }
    
    public void setCitaFechaHora(LocalDateTime citaFechaHora) {
        this.citaFechaHora = citaFechaHora;
    }
    
    public String getCitaEstado() {
        return citaEstado;
    }
    
    public void setCitaEstado(String citaEstado) {
        this.citaEstado = citaEstado;
    }
}
