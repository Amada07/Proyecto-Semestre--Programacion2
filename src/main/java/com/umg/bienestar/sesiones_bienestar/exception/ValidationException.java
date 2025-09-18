/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.exception;

/**
 *
 * @author amada
 */
public class ValidationException extends Exception {
    private String campo;
    private String valor;
    
    public ValidationException(String mensaje) {
        super(mensaje);
    }
    
    public ValidationException(String campo, String valor, String mensaje) {
        super(String.format("Error en campo '%s' con valor '%s': %s", campo, valor, mensaje));
        this.campo = campo;
        this.valor = valor;
    }
    
    public String getCampo() { return campo; }
    public String getValor() { return valor; }
} 

