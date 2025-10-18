/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.dto;
import java.math.BigDecimal;

/**
 *
 * @author amada
 */
public class ServicioRequest {
  private String codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer duracion;     
    private Integer cupoMaximo;   

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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
    
    @Override
    public String toString() {
        return "ServicioRequest{" +
            "codigo='" + codigo + '\'' +
            ", nombre='" + nombre + '\'' +
            ", precio=" + precio +
            ", duracion=" + duracion +
            ", cupoMaximo=" + cupoMaximo +
            '}';
    }
}
