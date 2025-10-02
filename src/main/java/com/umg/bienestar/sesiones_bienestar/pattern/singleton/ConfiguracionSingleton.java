/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.pattern.singleton;

/**
 *
 * @author amada
 */
public class ConfiguracionSingleton {
  private static ConfiguracionSingleton instancia;
    private String nombreSistema;
    private String version;
    private int maxCitasPendientesPorCliente;
    private int horasMinAnticipacion;
    private int horasLimiteCancelacion;
    
    private ConfiguracionSingleton() {
        this.nombreSistema = "Sistema de Gestión de Sesiones de Bienestar";
        this.version = "1.0.0";
        this.maxCitasPendientesPorCliente = 3;
        this.horasMinAnticipacion = 2;
        this.horasLimiteCancelacion = 24;
    }
    
    public static synchronized ConfiguracionSingleton getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionSingleton();
        }
        return instancia;
    }
    
    public String getNombreSistema() {
        return nombreSistema;
    }
    
    public String getVersion() {
        return version;
    }
    
    public int getMaxCitasPendientesPorCliente() {
        return maxCitasPendientesPorCliente;
    }
    
    public int getHorasMinAnticipacion() {
        return horasMinAnticipacion;
    }
    
    public int getHorasLimiteCancelacion() {
        return horasLimiteCancelacion;
    }
    
    public void setMaxCitasPendientesPorCliente(int max) {
        this.maxCitasPendientesPorCliente = max;
    }
}
  

