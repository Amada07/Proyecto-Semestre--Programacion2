/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.entity.Cliente;
import com.umg.bienestar.sesiones_bienestar.entity.RolUsuario;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ClienteRepository;
import com.umg.bienestar.sesiones_bienestar.service.IClienteService;
import com.umg.bienestar.sesiones_bienestar.service.IValidadorCliente;
import com.umg.bienestar.sesiones_bienestar.service.INotificadorService;
import com.umg.bienestar.sesiones_bienestar.exception.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ClienteServiceJpaImpl implements IClienteService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClienteServiceJpaImpl.class);
    
    // DIP - Depende de abstracciones
    private final ClienteRepository clienteRepository;
    private final IValidadorCliente validadorCliente;
    private final INotificadorService notificadorEmail;
    private final Validator validator;
    
    public ClienteServiceJpaImpl(ClienteRepository clienteRepository,
                                IValidadorCliente validadorCliente,
                                INotificadorService notificadorEmail,
                                Validator validator) {
        this.clienteRepository = clienteRepository;
        this.validadorCliente = validadorCliente;
        this.notificadorEmail = notificadorEmail;
        this.validator = validator;
    }
    
    @Override
    public Cliente crear(Cliente cliente) throws ValidationException {
        logger.info("Iniciando creación de cliente con email: {}", cliente.getEmail());
        
        try {
            // Validaciones de integridad ANTES de persistir
            validarIntegridad(cliente, null);
            
            // Establecer valores por defecto
            cliente.setRol(RolUsuario.CLIENTE);
            cliente.setActivo(true);
            
            // Validaciones Bean Validation
            validarConBeanValidation(cliente);
            
            // Guardar en PostgreSQL
            Cliente clienteCreado = clienteRepository.save(cliente);
            
            logger.info("Cliente creado exitosamente con ID: {}", clienteCreado.getId());
            
            // Enviar notificación (OCP - extensible)
            enviarNotificacionBienvenida(clienteCreado);
            
            return clienteCreado;
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad al crear cliente: {}", e.getMessage());
            throw new ValidationException("Error de integridad: " + extraerMensajeIntegridad(e));
        } catch (Exception e) {
            logger.error("Error inesperado al crear cliente: {}", e.getMessage(), e);
            throw new ValidationException("Error al crear cliente: " + e.getMessage());
        }
    }
    
    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        logger.debug("Buscando cliente por ID: {}", id);
        return clienteRepository.findById(id);
    }
    
    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        logger.debug("Buscando cliente por email: {}", email);
        return clienteRepository.findByEmail(email);
    }
    
    @Override
    public List<Cliente> listarTodos() {
        logger.debug("Listando todos los clientes");
        return clienteRepository.findAll();
    }
    
    @Override
    public List<Cliente> buscarPorNombre(String nombre) {
        logger.debug("Buscando clientes por nombre: {}", nombre);
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    public Cliente actualizar(Cliente cliente) throws ValidationException, ResourceNotFoundException {
        logger.info("Actualizando cliente con ID: {}", cliente.getId());
        
        try {
            // Verificar que existe
            Cliente clienteExistente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", cliente.getId().toString()));
            
            // Validaciones de integridad
            validarIntegridad(cliente, cliente.getId());
            
            // Validaciones Bean Validation
            validarConBeanValidation(cliente);
            
            // Mantener datos sensibles del cliente existente si no se envían
            if (cliente.getPassword() == null) {
                cliente.setPassword(clienteExistente.getPassword());
            }
            if (cliente.getFechaCreacion() == null) {
                cliente.setFechaCreacion(clienteExistente.getFechaCreacion());
            }
            
            // Actualizar en PostgreSQL
            Cliente clienteActualizado = clienteRepository.save(cliente);
            
            logger.info("Cliente actualizado exitosamente");
            
            // Notificar actualización
            enviarNotificacionActualizacion(clienteActualizado);
            
            return clienteActualizado;
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad al actualizar cliente: {}", e.getMessage());
            throw new ValidationException("Error de integridad: " + extraerMensajeIntegridad(e));
        }
    }
    
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BusinessException {
        logger.info("Eliminando cliente con ID: {}", id);
        
        try {
            Cliente cliente = clienteRepository.findByIdWithCitas(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id.toString()));
            
            // Validar reglas de negocio
            if (clienteRepository.tieneCitasActivas(id)) {
                throw new BusinessException("No se puede eliminar el cliente porque tiene citas pendientes o confirmadas");
            }
            
            // Eliminar físicamente de PostgreSQL
            clienteRepository.deleteById(id);
            
            logger.info("Cliente eliminado exitosamente");
            
            // Notificar eliminación
            enviarNotificacionEliminacion(cliente);
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Error de integridad al eliminar cliente: {}", e.getMessage());
            throw new BusinessException("No se puede eliminar el cliente debido a restricciones de integridad referencial");
        }
    }
    
    @Override
    public long contarClientes() {
        return clienteRepository.count();
    }
    
    // Métodos adicionales específicos para JPA
    public List<Cliente> listarClientesActivos() {
        return clienteRepository.findByActivoTrue();
    }
    
    public Optional<Cliente> buscarPorDpi(String dpi) {
        return clienteRepository.findByDpi(dpi);
    }
    
    // MÉTODOS PRIVADOS PARA VALIDACIONES DE INTEGRIDAD
    
    private void validarIntegridad(Cliente cliente, Long excludeId) throws ValidationException {
        logger.debug("Validando integridad para cliente: {}", cliente.getEmail());
        
        // Usar el validador especializado (SRP)
        validadorCliente.validarDatos(cliente);
        
        // Validaciones de unicidad a nivel de base de datos
        if (clienteRepository.existsByEmailExcludingId(cliente.getEmail(), excludeId)) {
            throw new ValidationException("email", cliente.getEmail(), "Ya existe un cliente con este email");
        }
        
        if (cliente.getDpi() != null && !cliente.getDpi().trim().isEmpty()) {
            Optional<Cliente> clienteConDpi = clienteRepository.findByDpi(cliente.getDpi());
            if (clienteConDpi.isPresent() && (excludeId == null || !clienteConDpi.get().getId().equals(excludeId))) {
                throw new ValidationException("dpi", cliente.getDpi(), "Ya existe un cliente con este DPI");
            }
        }
        
        if (cliente.getUsername() != null) {
            Optional<Cliente> clienteConUsername = clienteRepository.findByUsername(cliente.getUsername());
            if (clienteConUsername.isPresent() && (excludeId == null || !clienteConUsername.get().getId().equals(excludeId))) {
                throw new ValidationException("username", cliente.getUsername(), "Ya existe un usuario con este username");
            }
        }
    }
    
    private void validarConBeanValidation(Cliente cliente) throws ValidationException {
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Errores de validación: ");
            for (ConstraintViolation<Cliente> violation : violations) {
                sb.append(violation.getPropertyPath())
                  .append(": ")
                  .append(violation.getMessage())
                  .append("; ");
            }
            throw new ValidationException(sb.toString());
        }
    }
    
    private String extraerMensajeIntegridad(DataIntegrityViolationException e) {
        String mensaje = e.getMessage();
        
        if (mensaje.contains("clientes_email_key")) {
            return "Ya existe un cliente con este email";
        }
        if (mensaje.contains("clientes_dpi_key")) {
            return "Ya existe un cliente con este DPI";
        }
        if (mensaje.contains("usuarios_username_key")) {
            return "Ya existe un usuario con este username";
        }
        
        return "Violación de restricción de integridad en la base de datos";
    }
    
    // MÉTODOS DE NOTIFICACIÓN (OCP)
    
    private void enviarNotificacionBienvenida(Cliente cliente) {
        try {
            notificadorEmail.enviarNotificacion(
                "Bienvenido al Sistema de Bienestar",
                String.format("Hola %s, tu cuenta ha sido creada exitosamente. ID: %d", 
                             cliente.getNombre(), cliente.getId()),
                cliente.getId()
            );
        } catch (Exception e) {
            logger.warn("Error al enviar notificación de bienvenida: {}", e.getMessage());
        }
    }
    
    private void enviarNotificacionActualizacion(Cliente cliente) {
        try {
            notificadorEmail.enviarNotificacion(
                "Datos Actualizados",
                String.format("Hola %s, tus datos han sido actualizados exitosamente.", 
                             cliente.getNombre()),
                cliente.getId()
            );
        } catch (Exception e) {
            logger.warn("Error al enviar notificación de actualización: {}", e.getMessage());
        }
    }
    
    private void enviarNotificacionEliminacion(Cliente cliente) {
        try {
            notificadorEmail.enviarNotificacion(
                "Cuenta Eliminada",
                String.format("Hola %s, tu cuenta ha sido eliminada del sistema.", 
                             cliente.getNombre()),
                cliente.getId()
            );
        } catch (Exception e) {
            logger.warn("Error al enviar notificación de eliminación: {}", e.getMessage());
        }
    }
}