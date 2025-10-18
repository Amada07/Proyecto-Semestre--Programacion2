package com.umg.bienestar.cliente.models;

public class Notificacion {
    private Long id;
    private String tipo; // CITA_CONFIRMADA, CITA_PENDIENTE ,CITA_RECHAZADA,CITA_CANCELADA,  RECORDATORIO, FACTURA
    private String titulo;
    private String mensaje;
    private String fecha;
    private boolean leida;
    private Long referenciaId; // ID de la cita o factura relacionada

    public Notificacion() {}

    public Notificacion(String tipo, String titulo, String mensaje, String fecha) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = false;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public void setReferenciaId(Long referenciaId) {
        this.referenciaId = referenciaId;
    }
}