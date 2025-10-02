/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.controller;

import com.umg.bienestar.sesiones_bienestar.dto.CitaDTO;
import com.umg.bienestar.sesiones_bienestar.entity.Cita;
import com.umg.bienestar.sesiones_bienestar.entity.EstadoCita;
import com.umg.bienestar.sesiones_bienestar.service.impl.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/citas")
@Tag(name = "Citas", description = "API para gestión de citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @PostMapping
    @Operation(summary = "Agendar nueva cita", description = "UC-W03/UC-M04: Agendar cita o sesión")
    public ResponseEntity<Cita> agendar(@Valid @RequestBody CitaDTO citaDTO) {
        Cita cita = citaService.agendarCita(
            citaDTO.getClienteId(),
            citaDTO.getServicioId(),
            citaDTO.getFechaHora(),
            citaDTO.getNotas()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(cita);
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar cita", description = "UC-W05: Gestionar solicitudes de cita")
    public ResponseEntity<Cita> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.confirmar(id));
    }

    @PatchMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar cita", description = "UC-W05: Gestionar solicitudes de cita")
    public ResponseEntity<Cita> rechazar(@PathVariable Long id, @RequestParam String motivo) {
        return ResponseEntity.ok(citaService.rechazar(id, motivo));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar cita", description = "UC-W04: Cancelar cita/sesión")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        citaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cita por ID")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar citas de un cliente", description = "UC-M05: Ver historial de sesiones")
    public ResponseEntity<List<Cita>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(citaService.listarPorCliente(clienteId));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar citas por estado")
    public ResponseEntity<List<Cita>> listarPorEstado(@PathVariable EstadoCita estado) {
        return ResponseEntity.ok(citaService.listarPorEstado(estado));
    }

    @GetMapping("/historial/{clienteId}")
    @Operation(summary = "Obtener historial de sesiones", description = "UC-W07: Consultar historial de sesiones")
    public ResponseEntity<List<Cita>> obtenerHistorial(
        @PathVariable Long clienteId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        return ResponseEntity.ok(citaService.obtenerHistorialCliente(clienteId, inicio, fin));
    }
}
