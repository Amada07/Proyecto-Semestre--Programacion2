/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.entity;

import com.umg.bienestar.sesiones_bienestar.model.RolUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author amada
 */
@Entity
@Table(name="usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;
    
    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "fecha_ultimo_acceso")
    private LocalDateTime fechaUltimoAcceso;
    
    // Constructor protegido para JPA 
    protected Usuario() {}
    
    protected Usuario(String nombre, String email, String username, String password, RolUsuario rol) {
        this.nombre = nombre;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }
    
    // Método template pattern
    public final boolean puedeAcceder(String recurso) {
        if (!activo) {
            return false;
        }
        registrarAcceso();
        return tienePermisoParaRecurso(recurso);
    }
    
    // Método abstracto para implementar en subclases
    protected abstract boolean tienePermisoParaRecurso(String recurso);
    
    // Métodos de negocio
    public void registrarAcceso() {
        this.fechaUltimoAcceso = LocalDateTime.now();
    }
    
    public void activar() {
        this.activo = true;
    }
    
    public void desactivar() {
        this.activo = false;
    }
    
    // Callback JPA para validaciones adicionales
    @PrePersist
    @PreUpdate
    private void validarDatos() {
        if (username == null && email != null) {
            this.username = email;
        }
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = email;
        if (this.username == null) {
            this.username = email;
        }
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public RolUsuario getRol() { return rol; }
    public void setRol(RolUsuario rol) { this.rol = rol; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
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
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("%s{id=%d, nombre='%s', email='%s', activo=%s}", 
                getClass().getSimpleName(), id, nombre, email, activo);
    }
}