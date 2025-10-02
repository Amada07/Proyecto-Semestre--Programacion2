/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service.impl;

import com.umg.bienestar.sesiones_bienestar.entity.Servicio;
import com.umg.bienestar.sesiones_bienestar.exception.ResourceNotFoundException;
import com.umg.bienestar.sesiones_bienestar.exception.ValidationException;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


/**
 *
 * @author amada
 */


@Service
@Transactional
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public Servicio crear(Servicio servicio) {
        if (servicioRepository.existsByCodigo(servicio.getCodigo())) {
            throw new ValidationException("El código del servicio ya existe");
        }
        return servicioRepository.save(servicio);
    }

    public Servicio obtenerPorId(Long id) {
        return servicioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));
    }

    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    public List<Servicio> listarActivos() {
        return servicioRepository.findByActivoTrue();
    }

    public Servicio actualizar(Long id, Servicio servicio) {
        Servicio servicioExistente = obtenerPorId(id);
        
        if (!servicioExistente.getCodigo().equals(servicio.getCodigo()) && 
            servicioRepository.existsByCodigo(servicio.getCodigo())) {
            throw new ValidationException("El código del servicio ya existe");
        }
        
        servicioExistente.setNombre(servicio.getNombre());
        servicioExistente.setDescripcion(servicio.getDescripcion());
        servicioExistente.setPrecio(servicio.getPrecio());
        servicioExistente.setDuracionMinutos(servicio.getDuracionMinutos());
        servicioExistente.setMaxConcurrentes(servicio.getMaxConcurrentes());
        
        return servicioRepository.save(servicioExistente);
    }

    public void desactivar(Long id) {
        Servicio servicio = obtenerPorId(id);
        servicio.setActivo(false);
        servicioRepository.save(servicio);
    }

    public void eliminar(Long id) {
        Servicio servicio = obtenerPorId(id);
        servicioRepository.delete(servicio);
    }
}
