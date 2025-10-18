package com.umg.bienestar.cliente.models;

public class LoginResponse {
    private String token;
    private String username;
    private String rol;
    private Long id;

    public LoginResponse() {}

    public LoginResponse(String token, String username, String rol, Long id) {
        this.token = token;
        this.username = username;
        this.rol = rol;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
