/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.entity;

import com.umg.bienestar.sesiones_bienestar.entity.Cita;
import jakarta.persistence.*;
import java.util.List;
/**
 *
 * @author amada
 */
@Entity
public class Cliente extends Usuario {
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private boolean activo=true;
    
  @OneToMany(mappedBy = "cliente")
    private List<Cita> citas;

    @OneToMany(mappedBy = "cliente")
    private List<Factura> facturas;

    @OneToMany(mappedBy = "cliente")
    private List<Notificacion> notificaciones;
    
    
    //Getters y Setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
}
