/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.model.Cliente;
import com.umg.bienestar.sesiones_bienestar.service.IValidadorCliente;
import com.umg.bienestar.sesiones_bienestar.repository1.IClienteRepository;
import com.umg.bienestar.sesiones_bienestar.exception.ValidationException;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;
import java.util.Optional;


/**
 *
 * @author amada
 */

@Service
public class ValidadorClienteImpl implements IValidadorCliente {
    
    private final IClienteRepository clienteRepository;
    
    // VALIDACIONES DE FORMATO
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern TELEFONO_PATTERN = 
            Pattern.compile("^[0-9]{8,15}$");
    private static final Pattern DPI_PATTERN = 
            Pattern.compile("^[0-9]{8,13}$");
    
    public ValidadorClienteImpl(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    @Override
    public void validarDatos(Cliente cliente) throws ValidationException {
        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser nulo");
        }
        
        // Validar campos obligatorios
        validarCampoObligatorio(cliente.getNombre(), "nombre");
        validarCampoObligatorio(cliente.getEmail(), "email");
        
        // Validar formatos
        validarEmail(cliente.getEmail());
        
        if (cliente.getDpi() != null && !cliente.getDpi().trim().isEmpty()) {
            validarDpi(cliente.getDpi());
        }
        
        if (cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty()) {
            validarTelefono(cliente.getTelefono());
        }
        
        // Validar longitudes
        if (cliente.getNombre().length() > 100) {
            throw new ValidationException("nombre", cliente.getNombre(), 
                    "El nombre no puede exceder 100 caracteres");
        }
        
        if (cliente.getDireccion() != null && cliente.getDireccion().length() > 200) {
            throw new ValidationException("direccion", cliente.getDireccion(), 
                    "La dirección no puede exceder 200 caracteres");
        }
    }
    
    @Override
    public void validarEmail(String email) throws ValidationException {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("email", email, "Formato de email inválido");
        }
    }
    
    @Override
    public void validarDpi(String dpi) throws ValidationException {
        if (!DPI_PATTERN.matcher(dpi).matches()) {
            throw new ValidationException("dpi", dpi, 
                    "El DPI debe tener entre 8 y 13 dígitos numéricos");
        }
    }
    
    @Override
    public void validarTelefono(String telefono) throws ValidationException {
        if (!TELEFONO_PATTERN.matcher(telefono).matches()) {
            throw new ValidationException("telefono", telefono, 
                    "El teléfono debe tener entre 8 y 15 dígitos numéricos");
        }
    }
    
    @Override
    public void validarUnicidadEmail(String email, Long idExcluir) throws ValidationException {
        // VALIDACIÓN DE UNICIDAD 
        Optional<Cliente> clienteExistente = clienteRepository.findByEmail(email);
        
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            // Si existe y no es el mismo que estamos actualizando
            if (idExcluir == null || !cliente.getId().equals(idExcluir)) {
                throw new ValidationException("email", email, 
                        "Ya existe un cliente con este email");
            }
        }
    }
    
    private void validarCampoObligatorio(String valor, String nombreCampo) throws ValidationException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidationException(nombreCampo, valor, 
                    "El " + nombreCampo + " es obligatorio");
        }
    }
}