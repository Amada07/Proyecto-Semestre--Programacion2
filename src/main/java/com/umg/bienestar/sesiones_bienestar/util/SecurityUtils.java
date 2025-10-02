/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.umg.bienestar.sesiones_bienestar.util;

import com.umg.bienestar.sesiones_bienestar.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author amada
 */

public class SecurityUtils {

    private SecurityUtils() {
        
    }

 
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        
        return null;
    }

    
     
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "SYSTEM";
        }

        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        return principal.toString();
    }

 
     
    public static String getCurrentUserRol() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "ANONYMOUS";
        }

        Object principal = authentication.getPrincipal();
        
        
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getRol();
        }

     
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("USER");
    }

    
    public static String getCurrentUser() {
        return getCurrentUsername();
    }

   
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }

    
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role) 
                                    || authority.getAuthority().equals(role));
    }
}