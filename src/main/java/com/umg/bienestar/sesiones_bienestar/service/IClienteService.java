/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service;

import com.umg.bienestar.sesiones_bienestar.exception.BusinessException;
import com.umg.bienestar.sesiones_bienestar.exception.ResourceNotFoundException;
import com.umg.bienestar.sesiones_bienestar.exception.ValidationException;
import com.umg.bienestar.sesiones_bienestar.model.Cliente;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */
public interface IClienteService {
   Cliente crear(Cliente cliente) throws ValidationException;
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorEmail(String email);
    List<Cliente> listarTodos();
    List<Cliente> buscarPorNombre(String nombre);
    Cliente actualizar(Cliente cliente) throws ValidationException, ResourceNotFoundException;
    void eliminar(Long id) throws ResourceNotFoundException, BusinessException;
    long contarClientes();  
}
