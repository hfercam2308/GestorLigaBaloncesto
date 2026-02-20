package com.universidad.ligabasket.controller;

import com.universidad.ligabasket.model.CondicionesMeteorologicas;
import com.universidad.ligabasket.model.DatosPabellon;
import com.universidad.ligabasket.model.Partido;
import com.universidad.ligabasket.service.PartidoService;
import com.universidad.ligabasket.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controlador REST para gestionar la información meteorológica de los partidos
@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;
    private final PartidoService partidoService;

    public WeatherController(WeatherService weatherService, PartidoService partidoService) {
        this.weatherService = weatherService;
        this.partidoService = partidoService;
    }

    // Devuelve el clima de un partido (usando datos guardados o consultando la API)
    @GetMapping("/partido/{partidoId}")
    public ResponseEntity<CondicionesMeteorologicas> obtenerClimaDePartido(@PathVariable String partidoId) {
        Partido partido = partidoService.obtenerPorId(partidoId);

        if (partido.getCondicionesMeteorologicas() != null) {
            return ResponseEntity.ok(partido.getCondicionesMeteorologicas());
        }

        DatosPabellon pabellon = partido.getDatosPabellon();
        if (pabellon != null) {
            CondicionesMeteorologicas clima;
            if (pabellon.getLatitud() != null && pabellon.getLongitud() != null) {
                clima = weatherService.obtenerClimaPorCoordenadas(pabellon.getLatitud(), pabellon.getLongitud(),
                        partido.getFecha(), partido.getHora());
            } else if (pabellon.getCiudad() != null && !pabellon.getCiudad().isEmpty()) {
                clima = weatherService.obtenerClimaPorCiudad(pabellon.getCiudad(), partido.getFecha(),
                        partido.getHora());
            } else {
                clima = new CondicionesMeteorologicas(20, "No disponible", 50, "01d");
            }
            return ResponseEntity.ok(clima);
        }

        return ResponseEntity.ok(new CondicionesMeteorologicas(20, "No disponible", 50, "01d"));
    }

    // Actualiza el clima del partido en la base de datos tras consultarlo en tiempo
    // real
    @PostMapping("/partido/{partidoId}/actualizar")
    public ResponseEntity<CondicionesMeteorologicas> actualizarClimaPartido(@PathVariable String partidoId) {
        Partido partido = partidoService.obtenerPorId(partidoId);
        DatosPabellon pabellon = partido.getDatosPabellon();

        if (pabellon != null) {
            CondicionesMeteorologicas clima;
            if (pabellon.getLatitud() != null && pabellon.getLongitud() != null) {
                clima = weatherService.obtenerClimaPorCoordenadas(pabellon.getLatitud(), pabellon.getLongitud(),
                        partido.getFecha(), partido.getHora());
            } else if (pabellon.getCiudad() != null && !pabellon.getCiudad().isEmpty()) {
                clima = weatherService.obtenerClimaPorCiudad(pabellon.getCiudad(), partido.getFecha(),
                        partido.getHora());
            } else {
                clima = new CondicionesMeteorologicas(20, "No disponible", 50, "01d");
            }
            partidoService.actualizarClima(partidoId, clima);
            return ResponseEntity.ok(clima);
        }

        return ResponseEntity.ok(new CondicionesMeteorologicas(20, "No disponible", 50, "01d"));
    }
}
