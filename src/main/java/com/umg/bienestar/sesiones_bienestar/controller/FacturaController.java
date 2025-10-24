/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.controller;

import com.umg.bienestar.sesiones_bienestar.dto.FacturaRequest;
import com.umg.bienestar.sesiones_bienestar.entity.EstadoFactura;
import com.umg.bienestar.sesiones_bienestar.entity.Factura;
import com.umg.bienestar.sesiones_bienestar.service.impl.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author amada
 */

@RestController
@RequestMapping("/api/facturas")
@Tag(name = "Facturas", description = "API para gestión de facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    
    @GetMapping
    @Operation(summary = "Listar todas las facturas")
    public ResponseEntity<List<Factura>> listarTodas() {
        return ResponseEntity.ok(facturaService.listarTodas());
    }
    
    @PostMapping
    @Operation(summary = "Generar factura", description = "UC-W08: Generar factura para una sesión atendida")
    public ResponseEntity<Factura> generarFactura(@RequestBody FacturaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(facturaService.generar(request.getCitaId()));
    }
    
    
    @PostMapping("/generar/{citaId}")
    @Operation(summary = "Generar factura", description = "UC-W08: Generar factura para una sesión atendida")
    public ResponseEntity<Factura> generar(@PathVariable Long citaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.generar(citaId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener factura por ID")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar facturas de un cliente", description = "UC-M06: Consultar facturas")
    public ResponseEntity<List<Factura>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(facturaService.listarPorCliente(clienteId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar facturas por estado")
    public ResponseEntity<List<Factura>> listarPorEstado(@PathVariable EstadoFactura estado) {
        return ResponseEntity.ok(facturaService.listarPorEstado(estado));
    }

    @PatchMapping("/{id}/pagar")
    @Operation(summary = "Marcar factura como pagada")
    public ResponseEntity<Factura> marcarPagada(@PathVariable Long id) {
        return ResponseEntity.ok(facturaService.marcarPagada(id));
    }

    @GetMapping("/ingresos")
    @Operation(summary = "Calcular ingresos por período", description = "UC-W09: Generar reporte general")
    public ResponseEntity<Double> calcularIngresos(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return ResponseEntity.ok(facturaService.calcularIngresos(inicio, fin));
    }
}

