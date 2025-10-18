/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.controller;

import com.umg.bienestar.sesiones_bienestar.dto.ServicioRequest;
import com.umg.bienestar.sesiones_bienestar.entity.Servicio;
import com.umg.bienestar.sesiones_bienestar.service.impl.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 *
 * @author amada
 */

@RestController
@RequestMapping("/api/servicios")
@Tag(name = "Servicios", description = "API para gestión de servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

@PostMapping
@Operation(summary = "Crear nuevo servicio", description = "UC-W02: Permite registrar un nuevo servicio")
public ResponseEntity<Servicio> crear(@Valid @RequestBody ServicioRequest request) {
    Servicio servicio = new Servicio();
    servicio.setCodigo(request.getCodigo());
    servicio.setNombre(request.getNombre());
    servicio.setDescripcion(request.getDescripcion());
    servicio.setPrecio(request.getPrecio());
    servicio.setDuracionMinutos(request.getDuracion());
    servicio.setMaxConcurrentes(request.getCupoMaximo());
    servicio.setActivo(true);

    Servicio creado = servicioService.crear(servicio);

    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
}

    @GetMapping
    @Operation(summary = "Listar todos los servicios")
    public ResponseEntity<List<Servicio>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar servicios activos", description = "UC-M03: Consultar servicios disponibles")
    public ResponseEntity<List<Servicio>> listarActivos() {
        return ResponseEntity.ok(servicioService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio por ID")
    public ResponseEntity<Servicio> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio", description = "UC-W06: Gestionar servicios")
    public ResponseEntity<Servicio> actualizar(@PathVariable Long id, @Valid @RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioService.actualizar(id, servicio));
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar servicio")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        servicioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar servicio")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

