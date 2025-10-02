/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.pattern.observer;

import com.umg.bienestar.sesiones_bienestar.entity.Notificacion;
import com.umg.bienestar.sesiones_bienestar.entity.TipoNotificacion;
import com.umg.bienestar.sesiones_bienestar.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author amada
 */

public abstract class NotificacionObserver {
    protected List<Notificacion> notificacionesPendientes = new ArrayList<>();
    
    public abstract void notificar(Usuario usuario, String titulo, String mensaje, TipoNotificacion tipo);
    
    public abstract boolean enviarPendientes();
    
    public List<Notificacion> getNotificacionesPendientes() {
        return notificacionesPendientes;
    }
}
