/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository1;

import com.umg.bienestar.sesiones_bienestar.model.Cita;
import com.umg.bienestar.sesiones_bienestar.model.EstadoCita;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */
public interface ICitaRepository {
   Cita save(Cita cita);
    Optional<Cita> findById(Long id);
    List<Cita> findAll();
    List<Cita> findByClienteId(Long clienteId);
    List<Cita> findByServicioId(Long servicioId);
    List<Cita> findByEstado(EstadoCita estado);
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Cita> findCitasEnFranjaHoraria(Long servicioId, LocalDateTime inicio, LocalDateTime fin);
    void deleteById(Long id);
    boolean existsById(Long id);
    long countByServicioIdAndFechaHoraBetween(Long servicioId, LocalDateTime inicio, LocalDateTime fin);
}


