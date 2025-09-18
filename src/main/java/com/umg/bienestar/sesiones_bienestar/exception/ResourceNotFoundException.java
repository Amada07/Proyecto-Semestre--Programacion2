/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.exception;

/**
 *
 * @author amada
 */
public class ResourceNotFoundException extends Exception{
  public ResourceNotFoundException(String recurso, String id) {
        super("No se encontró " + recurso + " con ID: " + id);
    }  
}
