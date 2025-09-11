/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository;

import com.umg.bienestar.sesiones_bienestar.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author amada
 */
public interface FacturaRepository extends JpaRepository<Factura,Long> {
    
}
