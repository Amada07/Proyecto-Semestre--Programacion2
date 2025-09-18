/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author amada
 */
public class Factura {
   private Long id;
    private String numero;
    private LocalDate fechaEmision;
    private double subtotal;
    private double impuestos;
    private double montoTotal;
    private EstadoFactura estado;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaVencimiento;
    
    // Referencias por ID
    private Long clienteId;
    private Long citaId;
    
    // Constructores
    public Factura() {
        this.estado = EstadoFactura.PENDIENTE;
        this.fechaEmision = LocalDate.now();
        this.fechaCreacion = LocalDateTime.now();
        this.fechaVencimiento = LocalDate.now().plusDays(30); // 30 días para pagar
    }
    
    public Factura(String numero, Long clienteId, double subtotal, double impuestos) {
        this();
        this.numero = numero;
        this.clienteId = clienteId;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        calcularMontoTotal();
    }
    
    // Métodos
    public void calcularMontoTotal() {
        this.montoTotal = this.subtotal + this.impuestos;
    }
    
    public void marcarComoPagada() {
        this.estado = EstadoFactura.PAGADA;
    }
    
    public void anular() {
        this.estado = EstadoFactura.ANULADA;
    }
    
    public boolean estaPagada() {
        return estado == EstadoFactura.PAGADA;
    }
    
    public boolean estaVencida() {
        return fechaVencimiento.isBefore(LocalDate.now()) && estado == EstadoFactura.PENDIENTE;
    }
    
    public String generarNumeroFactura() {
        return "FAC-" + LocalDate.now().toString().replace("-", "") + "-" + System.currentTimeMillis();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public LocalDate getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { 
        this.subtotal = subtotal;
        calcularMontoTotal();
    }
    
    public double getImpuestos() { return impuestos; }
    public void setImpuestos(double impuestos) { 
        this.impuestos = impuestos;
        calcularMontoTotal();
    }
    
    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
    
    public EstadoFactura getEstado() { return estado; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    
    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", montoTotal=" + montoTotal +
                ", estado=" + estado +
                ", clienteId=" + clienteId +
                '}';
    }
} 

