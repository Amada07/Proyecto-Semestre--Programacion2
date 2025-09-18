/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author amada
 */
public class Administrador extends Usuario {
  private String codigoEmpleado;
    private Set<String> permisos; // Usar Set para evitar duplicados
    
    // Constructores
    public Administrador() {
        super();
        this.permisos = new HashSet<>();
        inicializarPermisos();
    }
    
    public Administrador(String nombre, String email, String codigoEmpleado) {
        super(nombre, email);
        this.codigoEmpleado = codigoEmpleado;
        this.permisos = new HashSet<>();
        inicializarPermisos();
    }
    
    // Implementación del método abstracto
    @Override
    public RolUsuario getRol() {
        return RolUsuario.ADMINISTRADOR;
    }
    
    // Implementación del método abstracto
    @Override
    protected boolean tienePermisoParaRecurso(String recurso) {
        return permisos.contains("SUPER_ADMIN") || tienePermisoEspecifico(recurso);
    }
    
    private boolean tienePermisoEspecifico(String recurso) {
        // Mapeo de recursos a permisos
        switch (recurso) {
            case "clientes.gestionar":
                return permisos.contains("GESTIONAR_CLIENTES");
            case "servicios.gestionar":
                return permisos.contains("GESTIONAR_SERVICIOS");
            case "reportes.generar":
                return permisos.contains("GENERAR_REPORTES");
            case "facturas.generar":
                return permisos.contains("GENERAR_FACTURAS");
            default:
                return false;
        }
    }
    
    private void inicializarPermisos() {
        permisos.add("GESTIONAR_CLIENTES");
        permisos.add("GESTIONAR_SERVICIOS");
        permisos.add("GESTIONAR_CITAS");
        permisos.add("GENERAR_FACTURAS");
        permisos.add("GENERAR_REPORTES");
        permisos.add("GESTIONAR_USUARIOS");
        permisos.add("VER_AUDITORIA");
        permisos.add("CONFIGURAR_SISTEMA");
    }
    
    // Métodos de negocio específicos
    public boolean tienePermiso(String permiso) {
        return permisos.contains(permiso) && isActivo();
    }
    
    public void agregarPermiso(String permiso) {
        if (permiso != null && !permiso.trim().isEmpty()) {
            permisos.add(permiso.trim().toUpperCase());
        }
    }
    
    public void removerPermiso(String permiso) {
        permisos.remove(permiso);
    }
    
    public void otorgarPermisoCompleto() {
        permisos.add("SUPER_ADMIN");
    }
    
    public boolean esSuperAdmin() {
        return permisos.contains("SUPER_ADMIN");
    }
    
    // Encapsulamiento - getter protegido
    public List<String> getPermisos() {
        return new ArrayList<>(permisos);
    }
    
    public void setPermisos(List<String> permisos) {
        this.permisos = new HashSet<>(permisos != null ? permisos : new ArrayList<>());
    }
    
    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }
    
    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", codigoEmpleado='" + codigoEmpleado + '\'' +
                ", activo=" + isActivo() +
                ", permisos=" + permisos.size() +
                ", esSuperAdmin=" + esSuperAdmin() +
                '}';
    }
}