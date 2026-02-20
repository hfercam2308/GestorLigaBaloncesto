package com.universidad.ligabasket.service;

import com.universidad.ligabasket.dto.LoginRequest;
import com.universidad.ligabasket.dto.LoginResponse;
import com.universidad.ligabasket.dto.RegisterRequest;
import com.universidad.ligabasket.model.Usuario;
import com.universidad.ligabasket.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

// Servicio encargado de la gestión de usuarios, registro e inicio de sesión
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registra un usuario nuevo validando que no exista y encriptando su password
    public Usuario registrar(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El username ya está en uso");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está en uso");
        }

        Usuario usuario = new Usuario(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()), // Hash de seguridad
                request.getRol(),
                request.getNombre(),
                request.getApellidos());

        return usuarioRepository.save(usuario);
    }

    // Valida las credenciales del usuario y devuelve sus datos básicos de sesión
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o contraseña incorrectos");
        }

        if (usuario.getActivo() == null || !usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario inactivo");
        }

        return new LoginResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getEquipoId());
    }
}
