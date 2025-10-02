/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.dto.ClienteDTO;
import com.umg.bienestar.sesiones_bienestar.entity.Cliente;
import com.umg.bienestar.sesiones_bienestar.exception.ResourceNotFoundException;
import com.umg.bienestar.sesiones_bienestar.exception.ValidationException;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ClienteRepository;
import com.umg.bienestar.sesiones_bienestar.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        
        Cliente cliente = new Cliente();
        cliente.setUsername(dto.getUsername());
        cliente.setPassword(passwordEncoder.encode(dto.getPassword()));
        cliente.setEmail(dto.getEmail());
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setDpi(dto.getDpi());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        cliente.setActivo(true);
        
        Cliente guardado = clienteRepository.save(cliente);
        
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
        return clienteRepository.findAll();
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
        cliente.setFechaNacimiento(dto.getFechaNacimiento());
        
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
        
        if (!validarDPIGuatemalteco(dto.getDpi())) {
            throw new ValidationException("El DPI guatemalteco no es válido");
        }
    }

    private boolean validarDPIGuatemalteco(String dpi) {
        if (dpi == null || dpi.length() != 13) {
            return false;
        }
        
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
