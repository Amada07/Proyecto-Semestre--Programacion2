/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amada
 */
public class Servicio {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private int duracionMinutos;
    private int maxConcurrentes;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Referencias por IDs
    private List<Long> citaIds;
    
    // Constructores
    public Servicio() {
        this.activo = true;
        this.maxConcurrentes = 1;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.citaIds = new ArrayList<>();
    }
    
    public Servicio(String codigo, String nombre, double precio, int duracionMinutos) {
        this();
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
    }
    
    // Métodos
    public void agregarCita(Long citaId) {
        if (!citaIds.contains(citaId)) {
            citaIds.add(citaId);
        }
    }
    
    public void removerCita(Long citaId) {
        citaIds.remove(citaId);
    }
    
    public boolean estaDisponible() {
        return activo && precio > 0 && duracionMinutos > 0;
    }
    
    public void desactivar() {
        this.activo = false;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void activar() {
        this.activo = true;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { 
        this.codigo = codigo;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { 
        this.nombre = nombre;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { 
        this.precio = precio;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { 
        this.duracionMinutos = duracionMinutos;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public int getMaxConcurrentes() { return maxConcurrentes; }
    public void setMaxConcurrentes(int maxConcurrentes) { this.maxConcurrentes = maxConcurrentes; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { 
        this.activo = activo;
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public List<Long> getCitaIds() { return new ArrayList<>(citaIds); }
    public void setCitaIds(List<Long> citaIds) { this.citaIds = new ArrayList<>(citaIds); }
    
    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", duracionMinutos=" + duracionMinutos +
                ", activo=" + activo +
                '}';
    }
}

