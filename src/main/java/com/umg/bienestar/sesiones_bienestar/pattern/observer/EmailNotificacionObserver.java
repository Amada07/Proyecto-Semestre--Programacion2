/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.pattern.observer;
import com.umg.bienestar.sesiones_bienestar.entity.Notificacion;
import com.umg.bienestar.sesiones_bienestar.entity.TipoNotificacion;
import com.umg.bienestar.sesiones_bienestar.entity.Usuario;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *
 * @author amada
 */
@Component
public class EmailNotificacionObserver extends NotificacionObserver {
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @Override
    public void notificar(Usuario usuario, String titulo, String mensaje, TipoNotificacion tipo) {
        Notificacion notificacion = new Notificacion(usuario, tipo, titulo, mensaje);
        notificacionesPendientes.add(notificacion);
        System.out.println("Email notificación creada para: " + usuario.getEmail());
    }
    
    @Override
    public boolean enviarPendientes() {
        for (Notificacion notif : notificacionesPendientes) {
            System.out.println("Enviando email a: " + notif.getUsuario().getEmail());
            System.out.println("Título: " + notif.getTitulo());
            System.out.println("Mensaje: " + notif.getMensaje());
            
            notif.setEnviada(true);
            notif.setFechaEnvio(LocalDateTime.now());
            
            notificacionRepository.save(notif);
        }
        notificacionesPendientes.clear();
        return true;
    }
}

