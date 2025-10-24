/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.dto;

/**
 *
 * @author amada
 */
public class FacturaRequest {
    private Long citaId;
    private String observaciones;
    
    // Constructor vacío
    public FacturaRequest() {
    }
    
    // Constructor con parámetros
    public FacturaRequest(Long citaId, String observaciones) {
        this.citaId = citaId;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public Long getCitaId() {
        return citaId;
    }
    
    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
