/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

/**
 *
 * @author amada
 */
public enum EstadoCita {
      PENDIENTE("Pendiete de confirmacion"),
      CONFIRMADA ("Confirmada"),
      CANCELADA ("Cancelada"),
      ATENDIDA ("Atendida");
      
private final String descripcion;

EstadoCita(String descripcion){
    this.descripcion= descripcion;
}
public String getDescripcion(){
    return descripcion;}
}