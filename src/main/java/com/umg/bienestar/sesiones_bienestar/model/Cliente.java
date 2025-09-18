/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amada
 */
public class Cliente extends Usuario{
   private String dpi;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    
    // Encapsulamiento - lista privada con métodos controlados
    private List<Long> citaIds;
    
    // Constructores
    public Cliente() {
        super();
        this.citaIds = new ArrayList<>();
    }
    
    public Cliente(String nombre, String email) {
        super(nombre, email);
        this.citaIds = new ArrayList<>();
    }
    
    // Implementación del método abstracto (Polimorfismo)
    @Override
    public RolUsuario getRol() {
        return RolUsuario.CLIENTE;
    }
    
    // Implementación del método abstracto
    @Override
    protected boolean tienePermisoParaRecurso(String recurso) {
        // Los clientes solo pueden acceder a sus propios recursos
        return recurso.startsWith("cliente.") || recurso.equals("servicios.consultar");
    }
    
    // Métodos específicos del cliente
    public void agregarCita(Long citaId) {
        if (citaId == null) {
            throw new IllegalArgumentException("El ID de cita no puede ser nulo");
        }
        if (!citaIds.contains(citaId)) {
            citaIds.add(citaId);
        }
    }
    
    public boolean removerCita(Long citaId) {
        return citaIds.remove(citaId);
    }
    
    public int getCantidadCitas() {
        return citaIds.size();
    }
    
    public boolean tieneCitas() {
        return !citaIds.isEmpty();
    }
    
   
    public int getEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    
    public boolean esMayorDeEdad() {
        return getEdad() >= 18;
    }
    
    // Encapsulamiento - getter 
    public List<Long> getCitaIds() {
        return new ArrayList<>(citaIds);
    }
    
    // Setter controlado
    public void setCitaIds(List<Long> citaIds) {
        this.citaIds = citaIds != null ? new ArrayList<>(citaIds) : new ArrayList<>();
    }
    
    // Getters y Setters 
    public String getDpi() { return dpi; }
    public void setDpi(String dpi) { this.dpi = dpi; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", edad=" + getEdad() +
                ", activo=" + isActivo() +
                ", cantidadCitas=" + getCantidadCitas() +
                '}';
    }
}
   