package com.umg.bienestar.sesiones_bienestar.repository.impl;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.umg.bienestar.sesiones_bienestar.model.Cliente;
import com.umg.bienestar.sesiones_bienestar.repository1.IClienteRepository;

import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 *
 * @author amada
 */

@Repository
public class ClienteRepositoryImpl implements IClienteRepository {
    
    // COLECCIONES PRINCIPALES (REQUERIMIENTO)
    private final Map<Long, Cliente> clientesPorId = new HashMap<>();       // HashMap para búsqueda rápida por ID
    private final Map<String, Cliente> clientesPorEmail = new HashMap<>();  // HashMap para búsqueda por email
    private final List<Cliente> listaClientes = new ArrayList<>();          // ArrayList para listados
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    // Constructor con datos de prueba
    public ClienteRepositoryImpl() {
        inicializarDatosPrueba();
    }
    
    private void inicializarDatosPrueba() {
        Cliente cliente1 = new Cliente("Juan Pérez", "juan@email.com");
        cliente1.setDpi("12345678");
        cliente1.setTelefono("12345678");
        save(cliente1);
        
        Cliente cliente2 = new Cliente("María García", "maria@email.com");
        cliente2.setDpi("87654321");
        cliente2.setTelefono("87654321");
        save(cliente2);
        
        Cliente cliente3 = new Cliente("Carlos López", "carlos@email.com");
        cliente3.setDpi("11223344");
        cliente3.setTelefono("11223344");
        save(cliente3);
    }
    
    @Override
    public Cliente save(Cliente cliente) {
        if (cliente.getId() == null) {
            // CREATE - nuevo cliente
            Long nuevoId = idGenerator.getAndIncrement();
            cliente.setId(nuevoId);
            
            // Almacenar en las 3 colecciones
            clientesPorId.put(nuevoId, cliente);
            clientesPorEmail.put(cliente.getEmail(), cliente);
            listaClientes.add(cliente);
        } else {
            // UPDATE - cliente existente
            Cliente clienteAnterior = clientesPorId.get(cliente.getId());
            
            if (clienteAnterior != null) {
                // Actualizar email en mapa si cambió
                if (!clienteAnterior.getEmail().equals(cliente.getEmail())) {
                    clientesPorEmail.remove(clienteAnterior.getEmail());
                    clientesPorEmail.put(cliente.getEmail(), cliente);
                }
                
                // Actualizar en mapa principal
                clientesPorId.put(cliente.getId(), cliente);
                
                // Actualizar en lista
                int indice = listaClientes.indexOf(clienteAnterior);
                if (indice >= 0) {
                    listaClientes.set(indice, cliente);
                }
            }
        }
        
        return cliente;
    }
    
    @Override
    public Optional<Cliente> findById(Long id) {
        return Optional.ofNullable(clientesPorId.get(id));
    }
    
    @Override
    public Optional<Cliente> findByEmail(String email) {
        return Optional.ofNullable(clientesPorEmail.get(email));
    }
    
    @Override
    public List<Cliente> findAll() {
        return new ArrayList<>(listaClientes); // Retorna copia para evitar modificaciones
    }
    
    @Override
    public List<Cliente> findByNombreContaining(String nombre) {
        return listaClientes.stream()
                .filter(cliente -> cliente.getNombre().toLowerCase()
                        .contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        Cliente cliente = clientesPorId.remove(id);
        if (cliente != null) {
            clientesPorEmail.remove(cliente.getEmail());
            listaClientes.remove(cliente);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        return clientesPorId.containsKey(id);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return clientesPorEmail.containsKey(email);
    }
    
    @Override
    public long count() {
        return listaClientes.size();
    }
}