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
import java.util.stream.Collectors;

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
    
      @GetMapping
    @Operation(summary = "Listar todas las citas o filtrar por estado")
    public ResponseEntity<List<CitaDTO>> listar(
        @RequestParam(required = false) EstadoCita estado
    ) {
        List<Cita> citas;
        
        if (estado != null) {
            citas = citaService.listarPorEstado(estado);
        } else {
            citas = citaService.listarTodas();
        }
        
        List<CitaDTO> citasDTO = citas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(citasDTO);
    }
    
    @PostMapping
    @Operation(summary = "Agendar nueva cita", description = "UC-W03/UC-M04: Agendar cita o sesión")
    public ResponseEntity<CitaDTO> agendar(@Valid @RequestBody CitaDTO citaDTO) {
        Cita cita = citaService.agendarCita(
            citaDTO.getClienteId(),
            citaDTO.getServicioId(),
            citaDTO.getFechaHora(),
            citaDTO.getNotas()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(convertirADTO(cita));
    }
   
    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar cita", description = "UC-W05: Gestionar solicitudes de cita")
    public ResponseEntity<CitaDTO> confirmar(@PathVariable Long id) {
        Cita cita = citaService.confirmar(id);
        return ResponseEntity.ok(convertirADTO(cita));
    }

    @PatchMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar cita", description = "UC-W05: Gestionar solicitudes de cita")
    public ResponseEntity<CitaDTO> rechazar(@PathVariable Long id, @RequestParam String motivo) {
        Cita cita = citaService.rechazar(id, motivo);
        return ResponseEntity.ok(convertirADTO(cita));
    }

    
    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar cita", description = "UC-W04: Cancelar cita/sesión")
    public ResponseEntity<CitaDTO> cancelar(@PathVariable Long id, @RequestParam String motivo) {
        Cita cita = citaService.cancelar(id, motivo);
        return ResponseEntity.ok(convertirADTO(cita));
    }

 
    @PatchMapping("/{id}/atender")
    @Operation(summary = "Marcar cita como atendida", description = "Cambiar estado de cita a ATENDIDA")
    public ResponseEntity<CitaDTO> marcarComoAtendida(@PathVariable Long id) {
        Cita cita = citaService.marcarComoAtendida(id);
        return ResponseEntity.ok(convertirADTO(cita));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cita por ID")
    public ResponseEntity<CitaDTO> obtenerPorId(@PathVariable Long id) {
        Cita cita = citaService.obtenerPorId(id);
        return ResponseEntity.ok(convertirADTO(cita));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar citas de un cliente", description = "UC-M05: Ver historial de sesiones")
    public ResponseEntity<List<CitaDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<Cita> citas = citaService.listarPorCliente(clienteId);
        List<CitaDTO> citasDTO = citas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citasDTO);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar citas por estado")
    public ResponseEntity<List<CitaDTO>> listarPorEstado(@PathVariable EstadoCita estado) {
        List<Cita> citas = citaService.listarPorEstado(estado);
        List<CitaDTO> citasDTO = citas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citasDTO);
    }

    @GetMapping("/historial/{clienteId}")
    @Operation(summary = "Obtener historial de sesiones", description = "UC-W07: Consultar historial de sesiones")
    public ResponseEntity<List<CitaDTO>> obtenerHistorial(
        @PathVariable Long clienteId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        List<Cita> citas = citaService.obtenerHistorialCliente(clienteId, inicio, fin);
        List<CitaDTO> citasDTO = citas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(citasDTO);
    }

    // ========================================
    // MÉTODO AUXILIAR PARA CONVERTIR A DTO
    // ========================================
    private CitaDTO convertirADTO(Cita cita) {
        if (cita == null) {
            return null;
        }
        
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado());
        dto.setNotas(cita.getNotas());
        dto.setMotivoRechazo(cita.getMotivoRechazo());
        dto.setFechaCancelacion(cita.getFechaCancelacion());
        dto.setFechaConfirmacion(cita.getFechaConfirmacion());
        dto.setFechaCreacion(cita.getFechaCreacion());
        dto.setFechaActualizacion(cita.getFechaActualizacion());
        
        // Información del cliente
        if (cita.getCliente() != null) {
            dto.setClienteId(cita.getCliente().getId());
            dto.setClienteNombre(cita.getCliente().getNombreCompleto());
            dto.setClienteEmail(cita.getCliente().getEmail());
            dto.setClienteTelefono(cita.getCliente().getTelefono());
        }
        
        // Información del servicio
        if (cita.getServicio() != null) {
            dto.setServicioId(cita.getServicio().getId());
            dto.setServicioNombre(cita.getServicio().getNombre());
            dto.setServicioPrecio(cita.getServicio().getPrecio());
            dto.setServicioDuracion(cita.getServicio().getDuracionMinutos());
        }
        
        return dto;
    }
}