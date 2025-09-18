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
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
   @Column(unique = true, nullable = false)
   private String codigo;
   
   @Column(nullable = false)
   private String nombre;
   
   private String descripcion;
   private double precio;
   private int duracion;
   private int maxConcurrents;
   private boolean activo = true;
   
   @OneToMany(mappedBy = "servicio")
    private List<Cita> citas;
   
   //Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getMaxConcurrents() {
        return maxConcurrents;
    }

    public void setMaxConcurrents(int maxConcurrents) {
        this.maxConcurrents = maxConcurrents;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
   
}
