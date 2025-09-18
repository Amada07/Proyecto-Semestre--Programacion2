/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository.jpa;

import com.umg.bienestar.sesiones_bienestar.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author amada
 */
public interface CitaRepository extends JpaRepository<Cita,Long> {
    
}
