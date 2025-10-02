/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.entity.*;
import com.umg.bienestar.sesiones_bienestar.exception.BusinessException;
import com.umg.bienestar.sesiones_bienestar.exception.ResourceNotFoundException;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.CitaRepository;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ClienteRepository;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.FacturaRepository;
import com.umg.bienestar.sesiones_bienestar.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 *
 * @author amada
 */

@Service
@Transactional
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;
    
    @Autowired
    private CitaRepository citaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private AuditoriaService auditoriaService;

    public Factura generar(Long citaId) {
        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
        
        if (cita.getEstado() != EstadoCita.ATENDIDA) {
            throw new BusinessException("Solo se pueden facturar sesiones atendidas");
        }
        
        if (facturaRepository.existsByCitaId(citaId)) {
            throw new BusinessException("Esta sesión ya fue facturada");
        }
        
        String numeroFactura = generarNumeroFactura();
        BigDecimal monto = cita.getServicio().getPrecio();
        BigDecimal impuestos = monto.multiply(new BigDecimal("0.12"));
        
        Factura factura = new Factura(
            numeroFactura,
            cita.getCliente(),
            cita,
            monto,
            LocalDateTime.now()
        );
        factura.setImpuestos(impuestos);
        factura.calcularMontoTotal();
        
        Factura guardada = facturaRepository.save(factura);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "GENERAR",
            "FACTURA",
            guardada.getId(),
            "Factura generada: " + guardada.getNumeroFactura() + " - Monto total: Q" + guardada.getMontoTotal()
        );
        
        return guardada;
    }

    public Factura obtenerPorId(Long id) {
        return facturaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con ID: " + id));
    }

    public List<Factura> listarPorCliente(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId);
    }

    public List<Factura> listarPorEstado(EstadoFactura estado) {
        return facturaRepository.findByEstado(estado);
    }

    public Factura marcarPagada(Long id) {
        Factura factura = obtenerPorId(id);
        factura.setEstado(EstadoFactura.PAGADA);
        factura.setFechaPago(LocalDateTime.now());
        
        Factura pagada = facturaRepository.save(factura);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "PAGAR",
            "FACTURA",
            pagada.getId(),
            "Factura marcada como pagada: " + pagada.getNumeroFactura()
        );
        
        return pagada;
    }

    public Double calcularIngresos(LocalDateTime inicio, LocalDateTime fin) {
        Double ingresos = facturaRepository.calcularIngresosPeriodo(inicio, fin);
        return ingresos != null ? ingresos : 0.0;
    }

    private String generarNumeroFactura() {
        long timestamp = System.currentTimeMillis();
        long count = facturaRepository.count() + 1;
        return String.format("FAC-%d-%05d", timestamp, count);
    }
}
