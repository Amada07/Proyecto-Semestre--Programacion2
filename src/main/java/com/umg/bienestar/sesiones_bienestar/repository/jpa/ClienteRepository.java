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
import java.util.Optional;
import java.util.List;

/**
 *
 * @author amada
 */

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Método agregado para CustomUserDetailsService
    Optional<Cliente> findByUsername(String username);
    
    Optional<Cliente> findByDpi(String dpi);
    Optional<Cliente> findByEmail(String email);
    boolean existsByDpi(String dpi);
    boolean existsByEmail(String email);
    List<Cliente> findByActivoTrue();
    
    @Query("SELECT c FROM Cliente c WHERE c.nombreCompleto LIKE %:nombre%")
    List<Cliente> buscarPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT COUNT(ci) > 0 FROM Cita ci WHERE ci.cliente.id = :clienteId AND ci.estado IN ('PENDIENTE', 'CONFIRMADA')")
    boolean tieneCitasActivas(@Param("clienteId") Long clienteId);
}