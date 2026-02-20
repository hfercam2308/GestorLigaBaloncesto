package com.universidad.ligabasket.controller;

import com.universidad.ligabasket.dto.LoginRequest;
import com.universidad.ligabasket.dto.LoginResponse;
import com.universidad.ligabasket.dto.RegisterRequest;
import com.universidad.ligabasket.model.Usuario;
import com.universidad.ligabasket.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controlador para el sistema de autenticación (Registro / Login)
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}