package com.umg.bienestar.cliente.models;

public class CitaRequest {
    private Long servicioId;
    private Long clienteId;
    private String fechaHora; // Formato: "2025-10-15T14:30:00"
    private String observaciones;

    public CitaRequest() {}

    public CitaRequest(Long servicioId, Long clienteId, String fechaHora, String observaciones) {
        this.servicioId = servicioId;
        this.clienteId = clienteId;
        this.fechaHora = fechaHora;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
