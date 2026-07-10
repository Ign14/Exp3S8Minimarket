package com.minimarket.security.model;

import java.util.List;

public class LoginResponse {

    private String token;
    private String tipo = "Bearer";
    private String username;
    private List<String> roles;
    private long expiraEnMs;

    public LoginResponse(String token, String username, List<String> roles, long expiraEnMs) {
        this.token = token;
        this.username = username;
        this.roles = roles;
        this.expiraEnMs = expiraEnMs;
    }

    public String getToken() { return token; }
    public String getTipo() { return tipo; }
    public String getUsername() { return username; }
    public List<String> getRoles() { return roles; }
    public long getExpiraEnMs() { return expiraEnMs; }
}
