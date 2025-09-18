/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

/**
 *
 * @author amada
 */
public interface IFacturable {
    Long getId();
    Long getClienteId();
    double calcularMonto();
    boolean esFacturable(); 
}
