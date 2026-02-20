package com.universidad.ligabasket.controller;

import com.universidad.ligabasket.dto.PartidoDTO;
import com.universidad.ligabasket.dto.ResultadoDTO;
import com.universidad.ligabasket.model.Partido;
import com.universidad.ligabasket.service.PartidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador para gestionar todas las operaciones de los partidos
@RestController
@RequestMapping("/api/partidos")
@CrossOrigin(origins = "*")
public class PartidoController {

    private final PartidoService partidoService;

    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    // Obtiene la lista simple de todos los partidos
    @GetMapping
    public ResponseEntity<List<Partido>> obtenerTodos() {
        return ResponseEntity.ok(partidoService.obtenerTodos());
    }

    // Obtiene los partidos con nombres de equipos (para la interfaz)
    @GetMapping("/dtos")
    public ResponseEntity<List<PartidoDTO>> obtenerTodosDTOs() {
        return ResponseEntity.ok(partidoService.obtenerTodosDTOs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partido> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(partidoService.obtenerPorId(id));
    }

    @GetMapping("/{id}/dto")
    public ResponseEntity<PartidoDTO> obtenerPorIdDTO(@PathVariable String id) {
        Partido partido = partidoService.obtenerPorId(id);
        return ResponseEntity.ok(partidoService.convertirADTO(partido));
    }

    @GetMapping("/jornada/{jornada}")
    public ResponseEntity<List<Partido>> obtenerPorJornada(@PathVariable Integer jornada) {
        return ResponseEntity.ok(partidoService.obtenerPorJornada(jornada));
    }

    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<Partido>> obtenerPorEquipo(@PathVariable String equipoId) {
        return ResponseEntity.ok(partidoService.obtenerPorEquipo(equipoId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Partido>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(partidoService.obtenerPorEstado(estado));
    }

    // Crea un nuevo partido en la agenda
    @PostMapping
    public ResponseEntity<Partido> crear(@RequestBody Partido partido) {
        return new ResponseEntity<>(partidoService.crear(partido), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partido> actualizar(@PathVariable String id, @RequestBody Partido partido) {
        return ResponseEntity.ok(partidoService.actualizar(id, partido));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Partido> cambiarEstado(@PathVariable String id, @RequestParam String estado) {
        return ResponseEntity.ok(partidoService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        partidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Registra el marcador final y finaliza el partido
    @PostMapping("/{id}/resultado")
    public ResponseEntity<Partido> registrarResultado(@PathVariable String id, @RequestBody ResultadoDTO resultado) {
        return ResponseEntity.ok(partidoService.registrarResultado(id, resultado));
    }
}
