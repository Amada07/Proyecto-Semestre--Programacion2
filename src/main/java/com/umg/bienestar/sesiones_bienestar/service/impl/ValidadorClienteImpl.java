/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.entity.Cliente;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ClienteRepository;
import com.umg.bienestar.sesiones_bienestar.service.IValidadorCliente;
import com.umg.bienestar.sesiones_bienestar.exception.ValidationException;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 *
 * @author amada
 */
@Service
public class ValidadorClienteJpaImpl implements IValidadorCliente {
    
    private static final Logger logger = LoggerFactory.getLogger(ValidadorClienteJpaImpl.class);
    
    private final ClienteRepository clienteRepository;
    
    // PATRONES DE VALIDACIÓN (mejorados para Guatemala)
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern TELEFONO_PATTERN = 
            Pattern.compile("^([0-9]{8}|[0-9]{4}-[0-9]{4}|\\+502[0-9]{8}|\\+502 [0-9]{4} [0-9]{4})$");
    
    private static final Pattern DPI_PATTERN = 
            Pattern.compile("^[0-9]{13}$"); // DPI Guatemala tiene exactamente 13 dígitos
    
    private static final Pattern NOMBRE_PATTERN = 
            Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{2,}$");
    
    public ValidadorClienteJpaImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    @Override
    public void validarDatos(Cliente cliente) throws ValidationException {
        logger.debug("Validando datos completos del cliente: {}", cliente.getEmail());
        
        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser nulo");
        }
        
        // Validar campos obligatorios
        validarCampoObligatorio(cliente.getNombre(), "nombre");
        validarCampoObligatorio(cliente.getEmail(), "email");
        
        // Validar formatos específicos
        validarEmail(cliente.getEmail());
        validarNombre(cliente.getNombre());
        
        if (cliente.getDpi() != null && !cliente.getDpi().trim().isEmpty()) {
            validarDpi(cliente.getDpi());
        }
        
        if (cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty()) {
            validarTelefono(cliente.getTelefono());
        }
        
        // Validar fecha de nacimiento
        if (cliente.getFechaNacimiento() != null) {
            validarFechaNacimiento(cliente.getFechaNacimiento());
        }
        
        // Validar longitudes
        validarLongitudes(cliente);
    }
    
    @Override
    public void validarEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("email", email, "El email es obligatorio");
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("email", email, "Formato de email inválido");
        }
        
        if (email.length() > 100) {
            throw new ValidationException("email", email, "El email no puede exceder 100 caracteres");
        }
    }
    
    @Override
    public void validarDpi(String dpi) throws ValidationException {
        if (dpi == null || dpi.trim().isEmpty()) {
            return; // DPI es opcional
        }
        
        String dpiLimpio = dpi.replaceAll("[^0-9]", "");
        
        if (!DPI_PATTERN.matcher(dpiLimpio).matches()) {
            throw new ValidationException("dpi", dpi, 
                    "El DPI debe tener exactamente 13 dígitos (formato Guatemala)");
        }
        
        // Validar algoritmo de DPI de Guatemala
        if (!validarAlgoritmoDPI(dpiLimpio)) {
            throw new ValidationException("dpi", dpi, "El DPI no es válido según el algoritmo guatemalteco");
        }
    }
    
    @Override
    public void validarTelefono(String telefono) throws ValidationException {
        if (telefono == null || telefono.trim().isEmpty()) {
            return; // Teléfono es opcional
        }
        
        if (!TELEFONO_PATTERN.matcher(telefono.trim()).matches()) {
            throw new ValidationException("telefono", telefono, 
                    "Formato de teléfono inválido. Use: 12345678, 1234-5678, +50212345678 o +502 1234 5678");
        }
    }
    
    @Override
    public void validarUnicidadEmail(String email, Long idExcluir) throws ValidationException {
        logger.debug("Validando unicidad de email: {} excluyendo ID: {}", email, idExcluir);
        
        // Usar consulta JPA optimizada
        Optional<Cliente> clienteExistente = clienteRepository.findByEmailExcludingId(email, idExcluir);
        
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            throw new ValidationException("email", email, 
                    String.format("Ya existe un cliente con este email (ID: %d)", cliente.getId()));
        }
    }
    
    // MÉTODOS AUXILIARES DE VALIDACIÓN
    
    private void validarNombre(String nombre) throws ValidationException {
        if (!NOMBRE_PATTERN.matcher(nombre.trim()).matches()) {
            throw new ValidationException("nombre", nombre, 
                    "El nombre solo puede contener letras y espacios");
        }
        
        if (nombre.trim().length() < 2) {
            throw new ValidationException("nombre", nombre, 
                    "El nombre debe tener al menos 2 caracteres");
        }
    }
    
    private void validarFechaNacimiento(LocalDate fechaNacimiento) throws ValidationException {
        LocalDate ahora = LocalDate.now();
        
        if (fechaNacimiento.isAfter(ahora)) {
            throw new ValidationException("fechaNacimiento", fechaNacimiento.toString(), 
                    "La fecha de nacimiento no puede ser futura");
        }
        
        int edad = Period.between(fechaNacimiento, ahora).getYears();
        if (edad > 120) {
            throw new ValidationException("fechaNacimiento", fechaNacimiento.toString(), 
                    "La edad no puede ser mayor a 120 años");
        }
        
        if (edad < 0) {
            throw new ValidationException("fechaNacimiento", fechaNacimiento.toString(), 
                    "Fecha de nacimiento inválida");
        }
    }
    
    private void validarLongitudes(Cliente cliente) throws ValidationException {
        if (cliente.getNombre() != null && cliente.getNombre().length() > 100) {
            throw new ValidationException("nombre", cliente.getNombre(), 
                    "El nombre no puede exceder 100 caracteres");
        }
        
        if (cliente.getDireccion() != null && cliente.getDireccion().length() > 200) {
            throw new ValidationException("direccion", cliente.getDireccion(), 
                    "La dirección no puede exceder 200 caracteres");
        }
        
        if (cliente.getUsername() != null && cliente.getUsername().length() > 50) {
            throw new ValidationException("username", cliente.getUsername(), 
                    "El username no puede exceder 50 caracteres");
        }
    }
    
    private void validarCampoObligatorio(String valor, String nombreCampo) throws ValidationException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidationException(nombreCampo, valor, 
                    "El " + nombreCampo + " es obligatorio");
        }
    }
    
    /**
     * Valida el algoritmo del DPI guatemalteco
     * El DPI utiliza un dígito verificador basado en módulo 11
     */
    private boolean validarAlgoritmoDPI(String dpi) {
        try {
            if (dpi.length() != 13) return false;
            
            // Los primeros 12 dígitos
            String numero = dpi.substring(0, 12);
            int digitoVerificador = Integer.parseInt(dpi.substring(12, 13));
            
            // Calcular dígito verificador
            int suma = 0;
            int multiplicador = 2;
            
            for (int i = 11; i >= 0; i--) {
                suma += Integer.parseInt(String.valueOf(numero.charAt(i))) * multiplicador;
                multiplicador++;
                if (multiplicador > 9) multiplicador = 2;
            }
            
            int residuo = suma % 11;
            int digitoCalculado = 11 - residuo;
            
            if (digitoCalculado == 11) digitoCalculado = 0;
            if (digitoCalculado == 10) digitoCalculado = 1;
            
            return digitoCalculado == digitoVerificador;
            
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    // MÉTODOS ADICIONALES PARA VALIDACIONES JPA
    
    public void validarUnicidadDpi(String dpi, Long idExcluir) throws ValidationException {
        if (dpi == null || dpi.trim().isEmpty()) {
            return;
        }
        
        Optional<Cliente> clienteExistente = clienteRepository.findByDpi(dpi);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            if (idExcluir == null || !cliente.getId().equals(idExcluir)) {
                throw new ValidationException("dpi", dpi, 
                        String.format("Ya existe un cliente con este DPI (ID: %d)", cliente.getId()));
            }
        }
    }
    
    public void validarUnicidadUsername(String username, Long idExcluir) throws ValidationException {
        if (username == null || username.trim().isEmpty()) {
            return;
        }
        
        Optional<Cliente> clienteExistente = clienteRepository.findByUsername(username);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            if (idExcluir == null || !cliente.getId().equals(idExcluir)) {
                throw new ValidationException("username", username, 
                        String.format("Ya existe un usuario con este username (ID: %d)", cliente.getId()));
            }
        }
    }
}