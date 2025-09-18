/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.service.INotificadorService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 *
 * @author amada
 */
@Service
@Primary
public class NotificadorEmailService implements INotificadorService {
   @Override
    public void enviarNotificacion(String titulo, String mensaje, Long clienteId) {
        // Simular envío de email
        System.out.println(" EMAIL enviado:");
        System.out.println("   Cliente ID: " + clienteId);
        System.out.println("   Título: " + titulo);
        System.out.println("   Mensaje: " + mensaje);
        System.out.println("   Enviado a: cliente" + clienteId + "@email.com");
        System.out.println("   ========================");
    }
    
    @Override
    public boolean soportaTipo(String tipo) {
        return "EMAIL".equalsIgnoreCase(tipo);
    }
    
    @Override
    public String getTipoNotificador() {
        return "EMAIL";
    }
}  
