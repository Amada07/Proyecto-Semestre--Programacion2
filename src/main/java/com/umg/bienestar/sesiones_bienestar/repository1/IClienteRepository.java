/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository1;

import com.umg.bienestar.sesiones_bienestar.model.Cliente;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */
public interface IClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(Long id);
    Optional<Cliente> findByEmail(String email);
    List<Cliente> findAll();
    List<Cliente> findByNombreContaining(String nombre);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    long count ();
}
