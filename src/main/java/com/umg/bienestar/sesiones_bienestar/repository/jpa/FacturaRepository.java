/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.jpa;

import com.umg.bienestar.sesiones_bienestar.entity.Factura;
import com.umg.bienestar.sesiones_bienestar.entity.EstadoFactura;
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
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByNumeroFactura(String numeroFactura);
    List<Factura> findByClienteId(Long clienteId);
    List<Factura> findByEstado(EstadoFactura estado);
    Optional<Factura> findByCitaId(Long citaId);
    boolean existsByCitaId(Long citaId);
    
    @Query("SELECT f FROM Factura f WHERE f.fechaEmision BETWEEN :inicio AND :fin")
    List<Factura> findByFechaEmisionBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    @Query("SELECT SUM(f.montoTotal) FROM Factura f WHERE f.estado = 'PAGADA' AND f.fechaEmision BETWEEN :inicio AND :fin")
    Double calcularIngresosPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
