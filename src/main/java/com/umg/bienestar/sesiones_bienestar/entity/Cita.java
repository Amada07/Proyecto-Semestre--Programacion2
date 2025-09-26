/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

/**
 *
 * @author amada
 */
@Entity
@Table(name="citas",indexes={
 @Index(name = "idx_cita_cliente_id", columnList = "cliente_id"),
    @Index(name = "idx_cita_servicio_id", columnList = "servicio_id"),
    @Index(name = "idx_cita_fecha_hora", columnList = "fecha_hora"),
    @Index(name = "idx_cita_estado", columnList = "estado") 
})
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
    
    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha y hora deben ser en el futuro")
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCita estado = EstadoCita.PENDIENTE;
    
    @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
    @Column(length = 500)
    private String notas;
    
    @Size(max = 200, message = "El motivo de rechazo no puede exceder 200 caracteres")
    @Column(name = "motivo_rechazo", length = 200)
    private String motivoRechazo;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "usuario_modificacion_id")
    private Long usuarioModificacionId;
    
    // Relaciones JPA
    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_cita_cliente"))
    private Cliente cliente;
    
    @NotNull(message = "El servicio es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_cita_servicio"))
    private Servicio servicio;
    
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Sesion sesion;
    
    // Constructores
    public Cita() {
        this.estado = EstadoCita.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public Cita(Cliente cliente, Servicio servicio, LocalDateTime fechaHora) {
        this();
        if (cliente == null || servicio == null || fechaHora == null) {
            throw new IllegalArgumentException("Cliente, servicio y fechaHora son obligatorios");
        }
        this.cliente = cliente;
        this.servicio = servicio;
        this.fechaHora = fechaHora;
    }
    
    // Métodos de negocio
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
    
    public boolean esFacturable() {
        return estado == EstadoCita.ATENDIDA;
    }
    
    public double calcularMonto() {
        if (servicio != null && esFacturable()) {
            return servicio.getPrecioAsDouble();
        }
        return 0.0;
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
    
    // Validaciones personalizadas
    @AssertTrue(message = "La fecha de la cita debe ser posterior a la fecha actual")
    public boolean isValidFechaFutura() {
        return fechaHora == null || fechaHora.isAfter(LocalDateTime.now());
    }
    
    // Callbacks JPA
    @PrePersist
    @PreUpdate
    private void validarDatos() {
        if (fechaHora != null && fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede crear/actualizar una cita con fecha en el pasado");
        }
    }
    
    // Getters y Setters
    public Long getId() { return id; }
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
    
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    
    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public Long getUsuarioModificacionId() { return usuarioModificacionId; }
    public void setUsuarioModificacionId(Long usuarioModificacionId) { this.usuarioModificacionId = usuarioModificacionId; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    
    public Sesion getSesion() { return sesion; }
    public void setSesion(Sesion sesion) { this.sesion = sesion; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cita cita = (Cita) obj;
        return id != null && id.equals(cita.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Cita{id=%d, fechaHora=%s, estado=%s, cliente=%s, servicio=%s}", 
                id, fechaHora, estado, 
                cliente != null ? cliente.getNombre() : "null",
                servicio != null ? servicio.getNombre() : "null");
    }
}