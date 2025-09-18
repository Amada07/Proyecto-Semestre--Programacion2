/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.service.INotificadorService;
import org.springframework.stereotype.Service;

/**
 *
 * @author amada
 */
@Service
public class NotificadorPushService implements INotificadorService{
   @Override
    public void enviarNotificacion(String titulo, String mensaje, Long clienteId) {
        // Simular notificación push
        System.out.println("PUSH enviada:");
        System.out.println("   Cliente ID: " + clienteId);
        System.out.println("   Título: " + titulo);
        System.out.println("   Mensaje: " + mensaje);
        System.out.println("   Dispositivo: Android-" + clienteId);
        System.out.println("   ========================");
    }
    
    @Override
    public boolean soportaTipo(String tipo) {
        return "PUSH".equalsIgnoreCase(tipo);
    }
    
    @Override
    public String getTipoNotificador() {
        return "PUSH";
    }
}  

