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
public abstract class Usuario {
    protected Long id;
    protected String nombre;
    protected String email;
    protected String username;
    protected String password;
    protected boolean activo;
    protected LocalDateTime fechaCreacion;
    protected LocalDateTime fechaUltimoAcceso;
    
    // Constructor protegido para las clases hijas
    protected Usuario() {
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    protected Usuario(String nombre, String email) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.username = email; 
    }
    
    // Método abstracto que deben implementar las clases hijas
    public abstract RolUsuario getRol();
    
    // Método template
    public final boolean puedeAcceder(String recurso) {
        if (!isActivo()) {
            return false;
        }
        
        registrarAcceso();
        return tienePermisoParaRecurso(recurso);
    }
    
    // Método que pueden sobrescribir las clases hijas
    protected abstract boolean tienePermisoParaRecurso(String recurso);
    
    // Método para todas las clases
    public void registrarAcceso() {
        this.fechaUltimoAcceso = LocalDateTime.now();
    }
    
    public void activar() {
        this.activo = true;
    }
    
    public void desactivar() {
        this.activo = false;
    }
    
    // Getters y Setters 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = email;
        this.username = email; 
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaUltimoAcceso() { return fechaUltimoAcceso; }
    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) { this.fechaUltimoAcceso = fechaUltimoAcceso; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return id != null && id.equals(usuario.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 

