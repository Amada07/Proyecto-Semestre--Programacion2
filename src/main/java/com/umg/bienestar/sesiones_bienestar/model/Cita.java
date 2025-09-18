/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

import java.time.LocalDateTime;

/**
 *
 * @author amada
 */
public class Cita implements IGestionable,IFacturable {
 private Long id;
    private LocalDateTime fechaHora;
    private EstadoCita estado;
    private String notas;
    private String motivoRechazo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Referencias por ID 
    private Long clienteId;
    private Long servicioId;
    private Long usuarioModificacionId;
    
     // Constructores
    public Cita() {
        this.estado = EstadoCita.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Cita(Long clienteId, Long servicioId, LocalDateTime fechaHora) {
        this();
        if (clienteId == null || servicioId == null || fechaHora == null) {
            throw new IllegalArgumentException("ClienteId, ServicioId y fechaHora son obligatorios");
        }
        this.clienteId = clienteId;
        this.servicioId = servicioId;
        this.fechaHora = fechaHora;
    }
    
    // Implementación de IGestionable
    @Override
    public void activar() {
        if (estado == EstadoCita.CANCELADA) {
            this.estado = EstadoCita.PENDIENTE;
            actualizarFecha();
        }
    }
    
    @Override
    public void desactivar() {
        if (puedeSerCancelada()) {
            this.estado = EstadoCita.CANCELADA;
            actualizarFecha();
        }
    }
    
    // Implementación de IFacturable
    @Override
    public double calcularMonto() {
        return 0.0;
    }
    
    @Override
    public boolean esFacturable() {
        return estado == EstadoCita.ATENDIDA;
    }
    
    // Métodos de negocio específicos
    public void confirmar(Long usuarioId) {
        validarTransicionEstado(EstadoCita.CONFIRMADA);
        this.estado = EstadoCita.CONFIRMADA;
        this.usuarioModificacionId = usuarioId;
        actualizarFecha();
    }
    
    public void cancelar(String motivo, Long usuarioId) {
        if (!puedeSerCancelada()) {
            throw new IllegalStateException("La cita no puede ser cancelada en su estado actual: " + estado);
        }
        this.estado = EstadoCita.CANCELADA;
        this.motivoRechazo = motivo;
        this.usuarioModificacionId = usuarioId;
        actualizarFecha();
    }
    
    public void marcarAtendida(Long usuarioId) {
        validarTransicionEstado(EstadoCita.ATENDIDA);
        this.estado = EstadoCita.ATENDIDA;
        this.usuarioModificacionId = usuarioId;
        actualizarFecha();
    }
    
    // Métodos de validación 
    public boolean puedeSerCancelada() {
        return estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA;
    }
    
    public boolean estaEnElPasado() {
        return fechaHora.isBefore(LocalDateTime.now());
    }
    
    public boolean estaEnLasProximas2Horas() {
        return fechaHora.isBefore(LocalDateTime.now().plusHours(2));
    }
    
    public boolean puedeSerModificada() {
        return estado == EstadoCita.PENDIENTE && !estaEnLasProximas2Horas();
    }
    
    // Método privado para validar transiciones
    private void validarTransicionEstado(EstadoCita nuevoEstado) {
        switch (nuevoEstado) {
            case CONFIRMADA:
                if (estado != EstadoCita.PENDIENTE) {
                    throw new IllegalStateException("Solo se pueden confirmar citas pendientes");
                }
                break;
            case ATENDIDA:
                if (estado != EstadoCita.CONFIRMADA) {
                    throw new IllegalStateException("Solo se pueden marcar como atendidas las citas confirmadas");
                }
                break;
        }
    }
    
    private void actualizarFecha() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters y Setters 
 @Override
    public Long getId() { return id; }
 @Override
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { 
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora no pueden ser nulas");
        }
        this.fechaHora = fechaHora;
        actualizarFecha();
    }
    
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { 
        this.estado = estado;
        actualizarFecha();
    }
    
    @Override
    public boolean isActivo() {
        return estado != EstadoCita.CANCELADA;
    }
    
    @Override
    public void setActivo(boolean activo) {
        if (!activo) {
            desactivar();
        }
    }
    
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    
    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
    
 @Override
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
 @Override
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
 @Override
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public Long getServicioId() { return servicioId; }
    public void setServicioId(Long servicioId) { this.servicioId = servicioId; }
    
    public Long getUsuarioModificacionId() { return usuarioModificacionId; }
    public void setUsuarioModificacionId(Long usuarioModificacionId) { this.usuarioModificacionId = usuarioModificacionId; }
    
    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", fechaHora=" + fechaHora +
                ", estado=" + estado +
                ", clienteId=" + clienteId +
                ", servicioId=" + servicioId +
                ", esFacturable=" + esFacturable() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cita cita = (Cita) obj;
        return id != null && id.equals(cita.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}