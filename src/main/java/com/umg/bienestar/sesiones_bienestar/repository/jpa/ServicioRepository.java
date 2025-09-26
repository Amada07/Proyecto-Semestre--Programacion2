/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.jpa;

import com.umg.bienestar.sesiones_bienestar.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author amada
 */
public interface ServicioRepository extends JpaRepository<Servicio,Long>{
 
    // Consultas básicas con validaciones
    Optional<Servicio> findByCodigo(String codigo);
    
    // Verificar unicidad para validaciones
    boolean existsByCodigo(String codigo);
    @Query("SELECT COUNT(s) > 0 FROM Servicio s WHERE s.codigo = :codigo AND (:excludeId IS NULL OR s.id != :excludeId)")
    boolean existsByCodigoExcludingId(@Param("codigo") String codigo, @Param("excludeId") Long excludeId);
    
    // Filtros de negocio
    List<Servicio> findByActivoTrue();
    List<Servicio> findByActivoTrueOrderByNombre();
    List<Servicio> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    
    // Consultas con rangos de precio
    List<Servicio> findByPrecioBetweenAndActivoTrue(BigDecimal precioMin, BigDecimal precioMax);
    
    // Servicios más utilizados
    @Query("SELECT s FROM Servicio s LEFT JOIN s.citas c " +
           "WHERE s.activo = true " +
           "GROUP BY s ORDER BY COUNT(c) DESC")
    List<Servicio> findMasUtilizados();
    
    // Validar si servicio tiene citas asociadas
    @Query("SELECT COUNT(c) > 0 FROM Servicio s JOIN s.citas c WHERE s.id = :servicioId")
    boolean tieneCitasAsociadas(@Param("servicioId") Long servicioId);
}

