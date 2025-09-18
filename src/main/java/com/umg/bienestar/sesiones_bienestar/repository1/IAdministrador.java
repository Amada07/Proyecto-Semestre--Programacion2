/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository1;

import com.umg.bienestar.sesiones_bienestar.model.Administrador;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */
public interface IAdministrador {
    Administrador save(Administrador administrador);
    Optional<Administrador> findById(Long id);
    Optional<Administrador> findByEmail(String email);
    Optional<Administrador> findByUsername(String username);
    Optional<Administrador> findByCodigoEmpleado(String codigoEmpleado);
    List<Administrador> findAll();
    List<Administrador> findByActivoTrue();
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsByCodigoEmpleado(String codigoEmpleado); 
}
