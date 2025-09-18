/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service;

/**
 *
 * @author amada
 */
public interface INotificadorService {
   void enviarNotificacion(String titulo, String mensaje, Long clienteId);
   boolean soportaTipo(String tipo);
   String getTipoNotificador(); 
}
