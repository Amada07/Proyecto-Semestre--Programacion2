/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.impl;

import com.umg.bienestar.sesiones_bienestar.model.Cita;
import com.umg.bienestar.sesiones_bienestar.model.EstadoCita;
import com.umg.bienestar.sesiones_bienestar.repository1.ICitaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class CitaRepositoryImpl implements ICitaRepository {
    
    // COLECCIONES PARA CRUD (HashMap y ArrayList)
    private final Map<Long, Cita> citasPorId = new HashMap<>();           // Búsqueda rápida por ID
    private final Map<Long, List<Cita>> citasPorCliente = new HashMap<>(); // Agrupadas por cliente
    private final Map<Long, List<Cita>> citasPorServicio = new HashMap<>();// Agrupadas por servicio
    private final List<Cita> listaCitas = new ArrayList<>();              // Para listados completos
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public CitaRepositoryImpl() {
        inicializarDatosPrueba();
    }
    
    private void inicializarDatosPrueba() {
        // Crear algunas citas de prueba
        Cita cita1 = new Cita(1L, 1L, LocalDateTime.now().plusDays(1));
        cita1.setNotas("Primera cita de prueba");
        save(cita1);
        
        Cita cita2 = new Cita(2L, 1L, LocalDateTime.now().plusDays(2));
        cita2.setNotas("Segunda cita de prueba");
        save(cita2);
    }
    
    @Override
    public Cita save(Cita cita) {
        if (cita.getId() == null) {
            // CREATE
            Long nuevoId = idGenerator.getAndIncrement();
            cita.setId(nuevoId);
            
            // Almacenar en HashMap principal
            citasPorId.put(nuevoId, cita);
            
            // Almacenar en ArrayList
            listaCitas.add(cita);
            
            // Agrupar por cliente en HashMap
            citasPorCliente.computeIfAbsent(cita.getClienteId(), k -> new ArrayList<>()).add(cita);
            
            // Agrupar por servicio en HashMap  
            citasPorServicio.computeIfAbsent(cita.getServicioId(), k -> new ArrayList<>()).add(cita);
            
        } else {
            // UPDATE
            Cita citaAnterior = citasPorId.get(cita.getId());
            if (citaAnterior != null) {
                // Actualizar en HashMap principal
                citasPorId.put(cita.getId(), cita);
                
                // Actualizar en ArrayList
                int indice = listaCitas.indexOf(citaAnterior);
                if (indice >= 0) {
                    listaCitas.set(indice, cita);
                }
                
                // Actualizar agrupaciones si cambiaron cliente o servicio
                actualizarAgrupaciones(citaAnterior, cita);
            }
        }
        
        return cita;
    }
    
    @Override
    public Optional<Cita> findById(Long id) {
        return Optional.ofNullable(citasPorId.get(id)); // HashMap O(1)
    }
    
    @Override
    public List<Cita> findAll() {
        return new ArrayList<>(listaCitas); // ArrayList
    }
    
    @Override
    public List<Cita> findByClienteId(Long clienteId) {
        return new ArrayList<>(citasPorCliente.getOrDefault(clienteId, new ArrayList<>())); // HashMap agrupado
    }
    
    @Override
    public List<Cita> findByServicioId(Long servicioId) {
        return new ArrayList<>(citasPorServicio.getOrDefault(servicioId, new ArrayList<>())); // HashMap agrupado
    }
    
    @Override
    public List<Cita> findByEstado(EstadoCita estado) {
        return listaCitas.stream() // Stream sobre ArrayList
                .filter(cita -> cita.getEstado() == estado)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin) {
        return listaCitas.stream() // Stream sobre ArrayList
                .filter(cita -> cita.getFechaHora().isAfter(inicio) && cita.getFechaHora().isBefore(fin))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Cita> findCitasEnFranjaHoraria(Long servicioId, LocalDateTime inicio, LocalDateTime fin) {
        return citasPorServicio.getOrDefault(servicioId, new ArrayList<>()).stream() // HashMap + Stream
                .filter(cita -> cita.getFechaHora().isAfter(inicio) && cita.getFechaHora().isBefore(fin))
                .filter(cita -> cita.getEstado() == EstadoCita.CONFIRMADA || cita.getEstado() == EstadoCita.PENDIENTE)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        Cita cita = citasPorId.remove(id); // HashMap remove
        if (cita != null) {
            // Remover de ArrayList
            listaCitas.remove(cita);
            
            // Remover de agrupaciones
            citasPorCliente.getOrDefault(cita.getClienteId(), new ArrayList<>()).remove(cita);
            citasPorServicio.getOrDefault(cita.getServicioId(), new ArrayList<>()).remove(cita);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        return citasPorId.containsKey(id); // HashMap containsKey O(1)
    }
    
    @Override
    public long countByServicioIdAndFechaHoraBetween(Long servicioId, LocalDateTime inicio, LocalDateTime fin) {
        return findCitasEnFranjaHoraria(servicioId, inicio, fin).size();
    }
    
    private void actualizarAgrupaciones(Cita citaAnterior, Cita citaNueva) {
        // Si cambió el cliente
        if (!citaAnterior.getClienteId().equals(citaNueva.getClienteId())) {
            citasPorCliente.getOrDefault(citaAnterior.getClienteId(), new ArrayList<>()).remove(citaAnterior);
            citasPorCliente.computeIfAbsent(citaNueva.getClienteId(), k -> new ArrayList<>()).add(citaNueva);
        }
    }
}