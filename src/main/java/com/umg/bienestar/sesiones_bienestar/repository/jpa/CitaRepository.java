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
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author amada
 */

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByClienteId(Long clienteId);
    List<Cita> findByServicioId(Long servicioId);
    List<Cita> findByEstado(EstadoCita estado);
    List<Cita> findByClienteIdAndEstado(Long clienteId, EstadoCita estado);
    
    @Query("SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :inicio AND :fin")
    List<Cita> findByFechaHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.servicio.id = :servicioId AND c.fechaHora BETWEEN :inicio AND :fin AND c.estado IN ('PENDIENTE', 'CONFIRMADA')")
    Long contarCitasEnFranja(@Param("servicioId") Long servicioId, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.cliente.id = :clienteId AND c.estado = 'PENDIENTE'")
    Long contarCitasPendientesCliente(@Param("clienteId") Long clienteId);
    
    @Query("SELECT c FROM Cita c WHERE c.cliente.id = :clienteId AND c.fechaHora BETWEEN :inicio AND :fin ORDER BY c.fechaHora DESC")
    List<Cita> findHistorialCliente(@Param("clienteId") Long clienteId, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
