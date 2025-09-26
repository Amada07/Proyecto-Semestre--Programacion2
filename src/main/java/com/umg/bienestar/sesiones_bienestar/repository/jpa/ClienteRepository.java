/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.jpa;

import com.umg.bienestar.sesiones_bienestar.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */

 @Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Consultas básicas con validación de integridad
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByDpi(String dpi);
    Optional<Cliente> findByUsername(String username);
    
    // Verificar existencia para validaciones de unicidad
    boolean existsByEmail(String email);
    boolean existsByDpi(String dpi);
    boolean existsByUsername(String username);
    
    // Consultas con filtros
    List<Cliente> findByActivoTrue();
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    // Consultas con validaciones de integridad referencial
    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.citas WHERE c.id = :id")
    Optional<Cliente> findByIdWithCitas(@Param("id") Long id);
    
    @Query("SELECT c FROM Cliente c WHERE c.email = :email AND (:excludeId IS NULL OR c.id != :excludeId)")
    Optional<Cliente> findByEmailExcludingId(@Param("email") String email, @Param("excludeId") Long excludeId);
    
    // Contar clientes con citas
    @Query("SELECT COUNT(c) FROM Cliente c WHERE SIZE(c.citas) > 0")
    long countClientesConCitas();
    
    // Validar si cliente tiene citas pendientes o confirmadas
    @Query("SELECT COUNT(cita) > 0 FROM Cliente c JOIN c.citas cita " +
           "WHERE c.id = :clienteId AND cita.estado IN ('PENDIENTE', 'CONFIRMADA')")
    boolean tieneCitasActivas(@Param("clienteId") Long clienteId);
}