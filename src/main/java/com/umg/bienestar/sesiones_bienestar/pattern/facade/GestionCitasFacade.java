/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.pattern.facade;

import com.umg.bienestar.sesiones_bienestar.entity.*;
import com.umg.bienestar.sesiones_bienestar.pattern.observer.EmailNotificacionObserver;
import com.umg.bienestar.sesiones_bienestar.service.impl.CitaService;
import com.umg.bienestar.sesiones_bienestar.service.impl.FacturaService;
import com.umg.bienestar.sesiones_bienestar.service.impl.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
/**
 *
 * @author amada
 */


@Component
public class GestionCitasFacade {
    
    @Autowired
    private CitaService citaService;
    
    @Autowired
    private SesionService sesionService;
    
    @Autowired
    private FacturaService facturaService;
    
    @Autowired
    private EmailNotificacionObserver emailObserver;
    
    public Cita agendarYNotificar(Long clienteId, Long servicioId, LocalDateTime fechaHora, String notas) {
        Cita cita = citaService.agendarCita(clienteId, servicioId, fechaHora, notas);
        
        emailObserver.notificar(
            cita.getCliente(),
            "Solicitud de cita recibida",
            "Su solicitud de cita para " + cita.getServicio().getNombre() + 
            " el " + cita.getFechaHora() + " ha sido recibida.",
            TipoNotificacion.EMAIL
        );
        emailObserver.enviarPendientes();
        
        return cita;
    }
    
    public Cita confirmarYNotificar(Long citaId) {
        Cita cita = citaService.confirmar(citaId);
        
        emailObserver.notificar(
            cita.getCliente(),
            "Cita confirmada",
            "Su cita para " + cita.getServicio().getNombre() + 
            " el " + cita.getFechaHora() + " ha sido confirmada.",
            TipoNotificacion.EMAIL
        );
        emailObserver.enviarPendientes();
        
        return cita;
    }
    
    public void completarSesionYFacturar(Long citaId, String observaciones) {
        Sesion sesion = sesionService.iniciar(citaId);
        
        sesionService.finalizar(sesion.getId(), observaciones);
        
        Factura factura = facturaService.generar(citaId);
        
        emailObserver.notificar(
            sesion.getCita().getCliente(),
            "Sesión completada y factura generada",
            "Su sesión ha sido completada. Factura #" + factura.getNumeroFactura() + 
            " por Q" + factura.getMontoTotal() + " ha sido generada.",
            TipoNotificacion.EMAIL
        );
        emailObserver.enviarPendientes();
    }
}
