package com.universidad.ligabasket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String userId;
    private String username;
    private String rol;
    private String equipoId;
    private String mensaje;

    public LoginResponse(String userId, String username, String rol, String equipoId) {
        this.userId = userId;
        this.username = username;
        this.rol = rol;
        this.equipoId = equipoId;
        this.mensaje = "Login exitoso";
    }
}
