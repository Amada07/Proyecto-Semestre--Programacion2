/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

/**
 *
 * @author amada
 */
public enum RolUsuario {
    CLIENTE("Cliente"),
    ADMINISTRADOR ("Administrador"),
    RECEPCIONISTA("Recepcionista");
    
    private final String descripcion;
    
    RolUsuario(String descripcion){
        this.descripcion=descripcion;
    }
   //get
    public String getDescripcion(){
        return descripcion;}
    }

