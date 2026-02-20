package com.universidad.ligabasket.service;

import com.universidad.ligabasket.model.Equipo;
import com.universidad.ligabasket.repository.EquipoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Servicio encargado de la gestión de equipos y actualización de la clasificación
@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<Equipo> obtenerTodos() {
        return equipoRepository.findAll();
    }

    public Equipo obtenerPorId(String id) {
        if (id == null || id.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID de equipo no proporcionado");
        }
        return equipoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado con ID: " + id));
    }

    // Crea un equipo nuevo validando que el nombre sea único en la liga
    public Equipo crear(Equipo equipo) {
        if (equipoRepository.findByNombre(equipo.getNombre()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un equipo con ese nombre");
        }

        return equipoRepository.save(equipo);
    }

    public Equipo actualizar(String id, Equipo equipoActualizado) {
        Equipo equipo = obtenerPorId(id);

        equipo.setNombre(equipoActualizado.getNombre());
        equipo.setUniversidad(equipoActualizado.getUniversidad());
        equipo.setCiudad(equipoActualizado.getCiudad());
        equipo.setLogoUrl(equipoActualizado.getLogoUrl());
        equipo.setColorPrimario(equipoActualizado.getColorPrimario());
        equipo.setColorSecundario(equipoActualizado.getColorSecundario());

        return equipoRepository.save(equipo);
    }

    public void eliminar(String id) {
        Equipo equipo = obtenerPorId(id);
        equipoRepository.delete(equipo);
    }

    // Obtiene la lista de equipos ordenada por puntos para mostrar la tabla de
    // clasificación
    public List<Equipo> obtenerClasificacion() {
        return equipoRepository.findAllByOrderByPuntosDesc();
    }

    // Actualiza victorias, derrotas y puntos (+2 por victoria) tras un partido
    public void actualizarEstadisticas(String equipoId, boolean victoria) {
        Equipo equipo = obtenerPorId(equipoId);

        if (victoria) {
            equipo.setVictorias(equipo.getVictorias() + 1);
            equipo.setPuntos(equipo.getPuntos() + 2);
        } else {
            equipo.setDerrotas(equipo.getDerrotas() + 1);
        }

        equipoRepository.save(equipo);
    }
}
