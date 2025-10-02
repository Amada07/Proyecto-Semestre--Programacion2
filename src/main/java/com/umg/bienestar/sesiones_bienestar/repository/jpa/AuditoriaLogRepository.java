/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.jpa;
import com.umg.bienestar.sesiones_bienestar.entity.AuditoriaLog;
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
public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Long> {
    
    List<AuditoriaLog> findByUsuarioId(Long usuarioId);
    
    List<AuditoriaLog> findByEntidadAndEntidadId(String entidad, Long entidadId);
    
    @Query("SELECT a FROM AuditoriaLog a WHERE a.fechaOperacion BETWEEN :inicio AND :fin ORDER BY a.fechaOperacion DESC")
    List<AuditoriaLog> findByFechaOperacionBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT a FROM AuditoriaLog a WHERE a.usuarioId = :usuarioId AND a.fechaOperacion BETWEEN :inicio AND :fin ORDER BY a.fechaOperacion DESC")
    List<AuditoriaLog> findByUsuarioAndFecha(@Param("usuarioId") Long usuarioId, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}

