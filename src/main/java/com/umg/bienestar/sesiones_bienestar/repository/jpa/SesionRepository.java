/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.jpa;

import com.umg.bienestar.sesiones_bienestar.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    Optional<Sesion> findByCitaId(Long citaId);
    
    @Query("SELECT s FROM Sesion s WHERE s.cita.cliente.id = :clienteId ORDER BY s.fechaInicio DESC")
    List<Sesion> findByClienteId(@Param("clienteId") Long clienteId);
    
    @Query("SELECT s FROM Sesion s WHERE s.fechaInicio BETWEEN :inicio AND :fin")
    List<Sesion> findByFechaBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
