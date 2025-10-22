/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.dto.ClienteDTO;
import com.umg.bienestar.sesiones_bienestar.entity.Cliente;
import com.umg.bienestar.sesiones_bienestar.entity.RolUsuario;
import com.umg.bienestar.sesiones_bienestar.exception.ResourceNotFoundException;
import com.umg.bienestar.sesiones_bienestar.exception.ValidationException;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ClienteRepository;
import com.umg.bienestar.sesiones_bienestar.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
/**
 *
 * @author amada
 */
@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuditoriaService auditoriaService;

    private static final Pattern DPI_PATTERN = Pattern.compile("\\d{13}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Cliente registrarCliente(ClienteDTO dto) {
        validarCliente(dto);
        
        if (clienteRepository.existsByDpi(dto.getDpi())) {
            throw new ValidationException("El DPI ya está registrado");
        }
        
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("El email ya está registrado");
        }
       
        // CAMBIO 1: Crear Cliente vacío (no usar constructor con parámetros)
      
        Cliente cliente = new Cliente();
        
        // Setear datos del usuario base
        cliente.setUsername(dto.getUsername());
        cliente.setPassword(passwordEncoder.encode(dto.getPassword()));
        cliente.setEmail(dto.getEmail());
        
      
        // CAMBIO 2: Setear explícitamente el rol CLIENTE
        // Esto es CRÍTICO para la herencia JOINED
 
        cliente.setRol(RolUsuario.CLIENTE);
        cliente.setActivo(true);
        
      
        // CAMBIO 3: Setear datos específicos de Cliente
     
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setDpi(dto.getDpi());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        
      
        // CAMBIO 4: Convertir String a LocalDate
        // Con validación por si viene vacío
       
        if (dto.getFechaNacimiento() != null && !dto.getFechaNacimiento().isEmpty()) {
            LocalDate fechaNacimiento = LocalDate.parse(dto.getFechaNacimiento());
            cliente.setFechaNacimiento(fechaNacimiento);
        } else {
            // Si no viene fecha, usar fecha por defecto
            cliente.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        }
        
        System.out.println("=== DEBUG: Guardando Cliente ===");
        System.out.println("Username: " + cliente.getUsername());
        System.out.println("Email: " + cliente.getEmail());
        System.out.println("Nombre Completo: " + cliente.getNombreCompleto());
        System.out.println("DPI: " + cliente.getDpi());
        System.out.println("Rol: " + cliente.getRol());
        System.out.println("Telefono: " + cliente.getTelefono());
        
        // Guardar - Hibernate insertará en usuarios Y clientes automáticamente
        Cliente guardado = clienteRepository.save(cliente);
        
        System.out.println("=== DEBUG: Cliente Guardado ===");
        System.out.println("ID generado: " + guardado.getId());
        System.out.println("Rol confirmado en BD: " + guardado.getRol());
        System.out.println("Nombre guardado: " + guardado.getNombreCompleto());
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "CREAR",
            "CLIENTE",
            guardado.getId(),
            "Nuevo cliente registrado: " + guardado.getNombreCompleto() + " (DPI: " + guardado.getDpi() + ")"
        );
        
        return guardado;
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
    }

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        System.out.println("=== DEBUG: Listando clientes ===");
        System.out.println("Total clientes encontrados: " + clientes.size());
        clientes.forEach(c -> System.out.println(
            "Cliente ID " + c.getId() + 
            ": " + c.getNombreCompleto() + 
            " | Email: " + c.getEmail() + 
            " | Rol: " + c.getRol()
        ));
        return clientes;
    }

    public List<Cliente> listarActivos() {
        return clienteRepository.findByActivoTrue();
    }

    public Cliente actualizar(Long id, ClienteDTO dto) {
        Cliente cliente = obtenerPorId(id);
        
        if (!cliente.getDpi().equals(dto.getDpi()) && clienteRepository.existsByDpi(dto.getDpi())) {
            throw new ValidationException("El DPI ya está registrado");
        }
        
        if (!cliente.getEmail().equals(dto.getEmail()) && clienteRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("El email ya está registrado");
        }
        
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        cliente.setEmail(dto.getEmail());
        
        LocalDate fechaNacimiento = LocalDate.parse(dto.getFechaNacimiento());
        cliente.setFechaNacimiento(fechaNacimiento);
        
        Cliente actualizado = clienteRepository.save(cliente);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "ACTUALIZAR",
            "CLIENTE",
            actualizado.getId(),
            "Cliente actualizado: " + actualizado.getNombreCompleto()
        );
        
        return actualizado;
    }

    public void desactivar(Long id) {
        Cliente cliente = obtenerPorId(id);
        cliente.setActivo(false);
        clienteRepository.save(cliente);
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "DESACTIVAR",
            "CLIENTE",
            cliente.getId(),
            "Cliente desactivado: " + cliente.getNombreCompleto()
        );
    }

    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        
        if (clienteRepository.tieneCitasActivas(id)) {
            throw new ValidationException("No se puede eliminar cliente con citas activas");
        }
        
        auditoriaService.registrar(
            SecurityUtils.getCurrentUserId(),
            SecurityUtils.getCurrentUsername(),
            SecurityUtils.getCurrentUserRol(),
            "ELIMINAR",
            "CLIENTE",
            cliente.getId(),
            "Cliente eliminado: " + cliente.getNombreCompleto() + " (DPI: " + cliente.getDpi() + ")"
        );
        
        clienteRepository.delete(cliente);
    }

    private void validarCliente(ClienteDTO dto) {
        if (!DPI_PATTERN.matcher(dto.getDpi()).matches()) {
            throw new ValidationException("El DPI debe tener 13 dígitos");
        }
        
        if (!EMAIL_PATTERN.matcher(dto.getEmail()).matches()) {
            throw new ValidationException("El email no es válido");
        }
    }

    private boolean validarDPIGuatemalteco(String dpi) {
        try {
            int suma = 0;
            for (int i = 0; i < 12; i++) {
                suma += Character.getNumericValue(dpi.charAt(i)) * (13 - i);
            }
            
            int modulo = suma % 11;
            int digitoVerificador = (modulo == 0) ? 0 : 11 - modulo;
            
            return digitoVerificador == Character.getNumericValue(dpi.charAt(12));
        } catch (Exception e) {
            return false;
        }
    }
}