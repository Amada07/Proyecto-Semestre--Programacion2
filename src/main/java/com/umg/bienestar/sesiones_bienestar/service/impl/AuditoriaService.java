/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;
import com.umg.bienestar.sesiones_bienestar.entity.AuditoriaLog;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.AuditoriaLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author amada
 */
@Service
@Transactional
public class AuditoriaService {

    @Autowired
    private AuditoriaLogRepository auditoriaRepository;

    @Async
    public void registrar(Long usuarioId, String username, String rol, 
                         String operacion, String entidad, Long entidadId, 
                         String descripcion) {
        AuditoriaLog log = new AuditoriaLog(
            usuarioId, username, rol, operacion, 
            entidad, entidadId, descripcion
        );
        
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ipAddress = getClientIp(request);
                log.setIpAddress(ipAddress);
            }
        } catch (Exception e) {
        }
        
        auditoriaRepository.save(log);
    }

    public void registrarConDatos(Long usuarioId, String username, String rol, 
                                  String operacion, String entidad, Long entidadId, 
                                  String descripcion, String datosAdicionales) {
        AuditoriaLog log = new AuditoriaLog(
            usuarioId, username, rol, operacion, 
            entidad, entidadId, descripcion
        );
        log.setDatosAdicionales(datosAdicionales);
        
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ipAddress = getClientIp(request);
                log.setIpAddress(ipAddress);
            }
        } catch (Exception e) {
        }
        
        auditoriaRepository.save(log);
    }

    public List<AuditoriaLog> obtenerPorUsuario(Long usuarioId) {
        return auditoriaRepository.findByUsuarioId(usuarioId);
    }

    public List<AuditoriaLog> obtenerPorEntidad(String entidad, Long entidadId) {
        return auditoriaRepository.findByEntidadAndEntidadId(entidad, entidadId);
    }

    public List<AuditoriaLog> obtenerPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return auditoriaRepository.findByFechaOperacionBetween(inicio, fin);
    }

    public List<AuditoriaLog> obtenerPorUsuarioYFecha(Long usuarioId, LocalDateTime inicio, LocalDateTime fin) {
        return auditoriaRepository.findByUsuarioAndFecha(usuarioId, inicio, fin);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}

