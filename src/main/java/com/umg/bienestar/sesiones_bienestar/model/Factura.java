/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

import jakarta.persistence.*;
import java.time.LocalDate;


/**
 *
 * @author amada
 */
@Entity
public class Factura {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @Column(unique = true, nullable = false)
   private String numero;
   
   private LocalDate fechaEmision;
   private double montoTotal;
   private String estado; //Pagada o Pendiente
   
   @ManyToOne
    @JoinColumn(name = "cliente_id")
   private Cliente cliente;
   
   @OneToOne
    @JoinColumn(name = "sesion_id")
   private Sesion sesion;
   
   //Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
   
}
