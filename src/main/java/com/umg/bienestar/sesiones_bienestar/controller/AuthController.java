/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.controller;

import com.umg.bienestar.sesiones_bienestar.dto.LoginRequest;
import com.umg.bienestar.sesiones_bienestar.dto.LoginResponse;
import com.umg.bienestar.sesiones_bienestar.entity.Usuario;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.UsuarioRepository;
import com.umg.bienestar.sesiones_bienestar.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author amada
 */

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "UC-02/UC-W09: Iniciar sesión web/móvil con JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario inactivo");
        }

        String token = jwtService.generateToken(
            usuario.getUsername(), 
            usuario.getId(), 
            usuario.getRol().name()
        );
        
        LoginResponse response = new LoginResponse(
            token,
            usuario.getUsername(),
            usuario.getRol().name(),
            usuario.getId()
        );

        return ResponseEntity.ok(response);
    }
}
