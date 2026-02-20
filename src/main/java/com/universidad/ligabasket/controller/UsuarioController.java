package com.universidad.ligabasket.controller;

import com.universidad.ligabasket.model.Usuario;
import com.universidad.ligabasket.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador para la gestión de usuarios y perfiles de jugadores
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> obtenerPorUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.obtenerPorUsername(username));
    }

    // Obtiene usuarios filtrados por su rol (ADMIN, JUGADOR)
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.obtenerPorRol(rol));
    }

    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<Usuario>> obtenerPorEquipo(@PathVariable String equipoId) {
        return ResponseEntity.ok(usuarioService.obtenerPorEquipo(equipoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable String id, @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/asignar-equipo/{equipoId}")
    public ResponseEntity<Void> asignarEquipo(@PathVariable String id, @PathVariable String equipoId) {
        usuarioService.asignarEquipo(id, equipoId);
        return ResponseEntity.ok().build();
    }

    // --- Endpoints de jugador ---

    @GetMapping("/jugadores")
    public ResponseEntity<List<Usuario>> obtenerJugadores() {
        return ResponseEntity.ok(usuarioService.obtenerJugadores());
    }

    // Endpoints específicos para jugadores (filtros por equipo o posición)
    @GetMapping("/jugadores/equipo/{equipoId}")
    public ResponseEntity<List<Usuario>> obtenerJugadoresPorEquipo(@PathVariable String equipoId) {
        return ResponseEntity.ok(usuarioService.obtenerJugadoresPorEquipo(equipoId));
    }

    @GetMapping("/jugadores/posicion/{posicion}")
    public ResponseEntity<List<Usuario>> obtenerJugadoresPorPosicion(@PathVariable String posicion) {
        return ResponseEntity.ok(usuarioService.obtenerJugadoresPorPosicion(posicion));
    }

    // Actualiza la ficha técnica del jugador (altura, peso, dorsal, etc.)
    @PutMapping("/{id}/datos-jugador")
    public ResponseEntity<Usuario> actualizarDatosJugador(@PathVariable String id, @RequestBody Usuario datosJugador) {
        return ResponseEntity.ok(usuarioService.actualizarDatosJugador(id, datosJugador));
    }
}
