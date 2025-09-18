/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

import java.time.LocalDateTime;

/**
 *
 * @author amada
 */
public interface IGestionable {
    Long getId();
    void setId(Long id);
    boolean isActivo();
    void setActivo(boolean activo);
    LocalDateTime getFechaCreacion();
    LocalDateTime getFechaActualizacion();
    void activar();
    void desactivar();
}
  
