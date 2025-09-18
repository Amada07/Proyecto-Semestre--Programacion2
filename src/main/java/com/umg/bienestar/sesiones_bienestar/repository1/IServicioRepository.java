/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository1;

import com.umg.bienestar.sesiones_bienestar.entity.Servicio;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */
public interface IServicioRepository {
    Servicio save(Servicio servicio);
    Optional<Servicio> findById(Long id);
    Optional<Servicio> findByCodigo(String codigo);
    List<Servicio> findAll();
    List<Servicio> findByActivoTrue();
    List<Servicio> findByNombreContaining(String nombre);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByCodigo(String codigo);
    long count();
}
