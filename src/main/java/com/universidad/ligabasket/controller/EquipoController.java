package com.universidad.ligabasket.controller;

import com.universidad.ligabasket.model.Equipo;
import com.universidad.ligabasket.service.EquipoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador para gestionar los equipos de la liga
@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    // Lista todos los equipos inscritos
    @GetMapping
    public ResponseEntity<List<Equipo>> obtenerTodos() {
        return ResponseEntity.ok(equipoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipo> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(equipoService.obtenerPorId(id));
    }

    // Devuelve los equipos ordenados por puntos (Tabla de posiciones)
    @GetMapping("/clasificacion")
    public ResponseEntity<List<Equipo>> obtenerClasificacion() {
        return ResponseEntity.ok(equipoService.obtenerClasificacion());
    }

    // Registra un nuevo equipo en el sistema
    @PostMapping
    public ResponseEntity<Equipo> crear(@RequestBody Equipo equipo) {
        return new ResponseEntity<>(equipoService.crear(equipo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipo> actualizar(@PathVariable String id, @RequestBody Equipo equipo) {
        return ResponseEntity.ok(equipoService.actualizar(id, equipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        equipoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
