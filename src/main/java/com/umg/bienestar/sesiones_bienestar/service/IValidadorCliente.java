/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.service;

import com.umg.bienestar.sesiones_bienestar.model.Cliente;
import com.umg.bienestar.sesiones_bienestar.exception.ValidationException;

/**
 *
 * @author amada
 */
public interface IValidadorCliente {
  void validarDatos(Cliente cliente) throws ValidationException;
    void validarEmail(String email) throws ValidationException;
    void validarDpi(String dpi) throws ValidationException;
    void validarTelefono(String telefono) throws ValidationException;
    void validarUnicidadEmail(String email, Long idExcluir) throws ValidationException;
} 