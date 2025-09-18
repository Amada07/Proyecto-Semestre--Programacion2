/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.repository1;

import com.umg.bienestar.sesiones_bienestar.model.EstadoFactura;
import com.umg.bienestar.sesiones_bienestar.model.Factura;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author amada
 */
public interface IFacturaRepository {
    Factura save(Factura factura);
    Optional<Factura> findById(Long id);
    Optional<Factura> findByNumero(String numero);
    List<Factura> findAll();
    List<Factura> findByClienteId(Long clienteId);
    List<Factura> findByEstado(EstadoFactura estado);
    List<Factura> findByFechaEmisionBetween(LocalDate inicio, LocalDate fin);
    List<Factura> findFacturasVencidas();
    void deleteById(Long id);
    boolean existsByNumero(String numero);
    double sumMontoTotalByClienteId(Long clienteId);   
}
