/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.dto;

/**
 *
 * @author amada
 */
public class SesionDTO {
private String observaciones;
    
    public SesionDTO() {}
    
    public SesionDTO(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}

