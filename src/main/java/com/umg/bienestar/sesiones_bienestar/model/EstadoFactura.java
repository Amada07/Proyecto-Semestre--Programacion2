/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

/**
 *
 * @author amada
 */
public enum EstadoFactura {
   PENDIENTE("Pendiente de pago"),
   PAGADA("Pagada"),
   VENCIDA("Vencida"),
    ANULADA("Anulada");
    
    private final String descripcion;
    
    EstadoFactura(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() { return descripcion; }
}
