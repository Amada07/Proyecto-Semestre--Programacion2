/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.List;
/**
 *
 * @author amada
 */

@Entity
@Table(name="servicios",indexes={
  @Index(name = "idx_servicio_codigo", columnList = "codigo"),
    @Index(name = "idx_servicio_activo", columnList = "activo")  
})
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
   @NotBlank(message = "El código del servicio es obligatorio")
    @Size(min = 3, max = 20, message = "El código debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "El código solo puede contener letras mayúsculas, números, guiones y guiones bajos")
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;
    
    @NotBlank(message = "El nombre del servicio es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "99999.99", message = "El precio no puede exceder 99,999.99")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @NotNull(message = "La duración es obligatoria")
    @Min(value = 15, message = "La duración mínima es 15 minutos")
    @Max(value = 480, message = "La duración máxima es 8 horas (480 minutos)")
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;
    
    @NotNull(message = "El máximo de concurrentes es obligatorio")
    @Min(value = 1, message = "Debe permitir al menos 1 cliente concurrente")
    @Max(value = 50, message = "No puede exceder 50 clientes concurrentes")
    @Column(name = "max_concurrentes", nullable = false)
    private Integer maxConcurrentes = 1;
    
    @NotNull
    @Column(nullable = false)
    private Boolean activo = true;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    // Relaciones JPA
    @OneToMany(mappedBy = "servicio", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Cita> citas = new ArrayList<>();
    
    // Constructores
    public Servicio() {}
    
    public Servicio(String codigo, String nombre, BigDecimal precio, Integer duracionMinutos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
    }
    
    // Métodos de negocio
    public void agregarCita(Cita cita) {
        if (cita != null && !citas.contains(cita)) {
            citas.add(cita);
            cita.setServicio(this);
        }
    }
    
    public void removerCita(Cita cita) {
        if (citas.remove(cita)) {
            cita.setServicio(null);
        }
    }
    
    public boolean estaDisponible() {
        return activo && precio.compareTo(BigDecimal.ZERO) > 0 && duracionMinutos > 0;
    }
    
    public void desactivar() {
        this.activo = false;
    }
    
    public void activar() {
        this.activo = true;
    }
    
    public double getPrecioAsDouble() {
        return precio != null ? precio.doubleValue() : 0.0;
    }
    
    public long getCantidadCitasActivas() {
        return citas.stream()
                   .filter(cita -> cita.getEstado() != EstadoCita.CANCELADA)
                   .count();
    }
    
    // Validaciones personalizadas
    @AssertTrue(message = "Un servicio activo debe tener precio y duración válidos")
    public boolean isValidServicioActivo() {
        if (!activo) return true; // Si está inactivo, no validar
        return precio != null && precio.compareTo(BigDecimal.ZERO) > 0 && 
               duracionMinutos != null && duracionMinutos > 0;
    }
    
    // Callbacks JPA
    @PrePersist
    @PreUpdate
    private void validarDatos() {
        // Normalizar código a mayúsculas
        if (codigo != null) {
            codigo = codigo.toUpperCase().trim();
        }
        
        // Normalizar nombre
        if (nombre != null) {
            nombre = nombre.trim();
        }
        
        // Validar coherencia de datos
        if (precio != null && precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }
    
    @PostLoad
    private void postLoad() {
        if (citas == null) citas = new ArrayList<>();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    
    public Integer getMaxConcurrentes() { return maxConcurrentes; }
    public void setMaxConcurrentes(Integer maxConcurrentes) { this.maxConcurrentes = maxConcurrentes; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Servicio servicio = (Servicio) obj;
        return id != null && id.equals(servicio.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Servicio{id=%d, codigo='%s', nombre='%s', precio=%s, activo=%s}", 
                id, codigo, nombre, precio, activo);
    }
}