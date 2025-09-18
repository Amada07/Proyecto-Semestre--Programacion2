/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.service.IClienteService;
import com.umg.bienestar.sesiones_bienestar.model.Cliente;
import com.umg.bienestar.sesiones_bienestar.service.*;
import com.umg.bienestar.sesiones_bienestar.repository1.IClienteRepository;
import com.umg.bienestar.sesiones_bienestar.exception.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author amada
 */
@Service
public class ClienteServiceImpl implements IClienteService {
  // DIP - Depende de abstracciones (interfaces)
    private final IClienteRepository clienteRepository;
    private final IValidadorCliente validadorCliente;
    private final INotificadorService notificadorEmail;
    
    // Constructor injection (DIP)
    public ClienteServiceImpl(IClienteRepository clienteRepository, 
                             IValidadorCliente validadorCliente,
                             INotificadorService notificadorEmail) {
        this.clienteRepository = clienteRepository;
        this.validadorCliente = validadorCliente;
        this.notificadorEmail = notificadorEmail;
    }
    
    @Override
    public Cliente crear(Cliente cliente) throws ValidationException {
        // SRP - Delegar validación al validador especializado
        validadorCliente.validarDatos(cliente);
        validadorCliente.validarUnicidadEmail(cliente.getEmail(), null);
        
        // Guardar usando el repositorio (que usa HashMap/ArrayList)
        Cliente clienteCreado = clienteRepository.save(cliente);
        
        // Enviar notificación de bienvenida (OCP - extensible)
        notificadorEmail.enviarNotificacion(
            "Bienvenido al Sistema de Bienestar",
            "Hola " + cliente.getNombre() + ", tu cuenta ha sido creada exitosamente.",
            clienteCreado.getId()
        );
        
        return clienteCreado;
    }
    
    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContaining(nombre);
    }
    
    @Override
    public Cliente actualizar(Cliente cliente) throws ValidationException, ResourceNotFoundException {
        // Verificar que existe
        if (!clienteRepository.existsById(cliente.getId())) {
            throw new ResourceNotFoundException("Cliente", cliente.getId().toString());
        }
        
        // SRP - Delegar validación
        validadorCliente.validarDatos(cliente);
        validadorCliente.validarUnicidadEmail(cliente.getEmail(), cliente.getId());
        
        // Actualizar usando colecciones
        Cliente clienteActualizado = clienteRepository.save(cliente);
        
        // Notificar actualización
        notificadorEmail.enviarNotificacion(
            "Datos Actualizados",
            "Tus datos han sido actualizados exitosamente.",
            clienteActualizado.getId()
        );
        
        return clienteActualizado;
    }
    
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BusinessException {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        
        if (!cliente.isPresent()) {
            throw new ResourceNotFoundException("Cliente", id.toString());
        }
        
        // Regla de negocio: verificar si tiene citas
        Cliente clienteData = cliente.get();
        if (clienteData.getCantidadCitas() > 0) {
            throw new BusinessException("No se puede eliminar el cliente porque tiene citas asociadas");
        }
        
        // Eliminar de las colecciones
        clienteRepository.deleteById(id);
        
        // Notificar eliminación
        notificadorEmail.enviarNotificacion(
            "Cuenta Eliminada",
            "Tu cuenta ha sido eliminada del sistema.",
            id
        );
    }
    
    @Override
    public long contarClientes() {
        return clienteRepository.count();
    }
}
  

