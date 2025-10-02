/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.security;

import com.umg.bienestar.sesiones_bienestar.entity.Cliente;
import com.umg.bienestar.sesiones_bienestar.entity.Usuario;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.ClienteRepository;
import com.umg.bienestar.sesiones_bienestar.repository.jpa.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author amada
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        
        if (usuario != null) {
            return new CustomUserDetails(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getRol().name(), 
                usuario.getActivo()
            );
        }
        
       
        Cliente cliente = clienteRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        return new CustomUserDetails(
            cliente.getId(),
            cliente.getUsername(),
            cliente.getPassword(),
            "ROLE_CLIENTE",
            cliente.getActivo()
        );
    }
}
