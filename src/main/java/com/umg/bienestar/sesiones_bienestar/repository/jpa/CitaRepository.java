/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.jpa;

import com.umg.bienestar.sesiones_bienestar.entity.Cita;
import com.umg.bienestar.sesiones_bienestar.entity.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */
public interface CitaRepository extends JpaRepository<Cita,Long> {
 // Consultas básicas con joins optimizados
    @Query("SELECT c FROM Cita c JOIN FETCH c.cliente JOIN FETCH c.servicio WHERE c.id = :id")
    Optional<Cita> findByIdWithDetails(@Param("id") Long id);
    
    // Consultas por cliente
    List<Cita> findByClienteIdOrderByFechaHoraDesc(Long clienteId);
    
    @Query("SELECT c FROM Cita c JOIN FETCH c.servicio WHERE c.cliente.id = :clienteId " +
           "AND c.estado IN :estados ORDER BY c.fechaHora DESC")
    List<Cita> findByClienteIdAndEstadoIn(@Param("clienteId") Long clienteId, 
                                         @Param("estados") List<EstadoCita> estados);
    
    // Consultas por servicio con validaciones de disponibilidad
    List<Cita> findByServicioIdAndEstadoInOrderByFechaHora(Long servicioId, List<EstadoCita> estados);
    
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.servicio.id = :servicioId " +
           "AND c.fechaHora BETWEEN :inicio AND :fin " +
           "AND c.estado IN ('PENDIENTE', 'CONFIRMADA')")
    long countByServicioAndFranjaHoraria(@Param("servicioId") Long servicioId,
                                        @Param("inicio") LocalDateTime inicio,
                                        @Param("fin") LocalDateTime fin);
    
    // Consultas por estado y fecha
    List<Cita> findByEstadoAndFechaHoraBetweenOrderByFechaHora(EstadoCita estado, 
                                                              LocalDateTime inicio, 
                                                              LocalDateTime fin);
    
    // Validar conflictos de horario
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.servicio.id = :servicioId " +
           "AND c.fechaHora BETWEEN :inicio AND :fin " +
           "AND c.estado IN ('PENDIENTE', 'CONFIRMADA') " +
           "AND (:excludeId IS NULL OR c.id != :excludeId)")
    long countConflictosHorario(@Param("servicioId") Long servicioId,
                               @Param("inicio") LocalDateTime inicio,
                               @Param("fin") LocalDateTime fin,
                               @Param("excludeId") Long excludeId);
    
    // Citas próximas a vencer (para notificaciones)
    @Query("SELECT c FROM Cita c JOIN FETCH c.cliente WHERE c.estado = 'CONFIRMADA' " +
           "AND c.fechaHora BETWEEN :ahora AND :limite")
    List<Cita> findCitasParaNotificar(@Param("ahora") LocalDateTime ahora,
                                     @Param("limite") LocalDateTime limite);
    
    // Estadísticas
    @Query("SELECT c.estado, COUNT(c) FROM Cita c GROUP BY c.estado")
    List<Object[]> countByEstado();
    
    // Validar si cliente tiene citas en una fecha específica
    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.cliente.id = :clienteId " +
           "AND DATE(c.fechaHora) = DATE(:fecha) AND c.estado != 'CANCELADA'")
    boolean clienteTieneCitaEnFecha(@Param("clienteId") Long clienteId, 
                                   @Param("fecha") LocalDateTime fecha);
}
