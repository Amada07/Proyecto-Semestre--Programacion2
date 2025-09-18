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
@Service("notificadorSMS")
public class NotificadorSMSService implements INotificadorService{
  @Override
    public void enviarNotificacion(String titulo, String mensaje, Long clienteId) {
        // Simular envío de SMS
        System.out.println(" SMS enviado:");
        System.out.println("   Cliente ID: " + clienteId);
        System.out.println("   Mensaje: " + mensaje);
        System.out.println("   Número: +502-1234-" + String.format("%04d", clienteId));
        System.out.println("   ========================");
    }
    
    @Override
    public boolean soportaTipo(String tipo) {
        return "SMS".equalsIgnoreCase(tipo);
    }
    
    @Override
    public String getTipoNotificador() {
        return "SMS";
    }
}  

