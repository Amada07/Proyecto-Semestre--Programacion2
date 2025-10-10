/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author amada
 */
public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            System.out.println(" Intentando cargar archivo .env...");
            
            // Cargar archivo .env
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing() // No falla si no existe .env 
                    .load();

            // Convertir las variables a un Map
            Map<String, Object> dotenvMap = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                dotenvMap.put(entry.getKey(), entry.getValue());
                // Imprimir cada variable (ocultando passwords)
                if (entry.getKey().contains("PASSWORD") || entry.getKey().contains("SECRET")) {
                    System.out.println("  " + entry.getKey() + " = ********");
                } else {
                    System.out.println("  " + entry.getKey() + " = " + entry.getValue());
                }
            });

            // Agregar las variables al contexto de Spring
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(
                new MapPropertySource("dotenvProperties", dotenvMap)
            );

            System.out.println(" Variables de entorno cargadas desde .env correctamente");
            System.out.println(" Total de variables cargadas: " + dotenvMap.size());
            
        } catch (Exception e) {
            System.out.println("️ No se pudo cargar .env (usando variables del sistema)");
            System.out.println(" Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}