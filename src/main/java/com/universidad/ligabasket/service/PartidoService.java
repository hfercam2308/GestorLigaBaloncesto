package com.universidad.ligabasket.service;

import com.universidad.ligabasket.dto.PartidoDTO;
import com.universidad.ligabasket.dto.ResultadoDTO;
import com.universidad.ligabasket.model.*;
import com.universidad.ligabasket.repository.PartidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

// Servicio principal para la gestión de partidos, resultados y validaciones de agenda
@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final EquipoService equipoService;

    public PartidoService(PartidoRepository partidoRepository, EquipoService equipoService) {
        this.partidoRepository = partidoRepository;
        this.equipoService = equipoService;
    }

    // Recupera todos los partidos ordenados por fecha y hora
    public List<Partido> obtenerTodos() {
        return partidoRepository.findAllByOrderByFechaAscHoraAsc();
    }

    // Busca un partido por su ID único o lanza error si no existe
    public Partido obtenerPorId(String id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partido no encontrado con ID: " + id));
    }

    public List<Partido> obtenerPorJornada(Integer jornada) {
        return partidoRepository.findByJornada(jornada);
    }

    public List<Partido> obtenerPorEquipo(String equipoId) {
        return partidoRepository.findByEquipoId(equipoId);
    }

    public List<Partido> obtenerPorEstado(String estado) {
        return partidoRepository.findByEstado(estado);
    }

    // Crea un partido nuevo tras validar que no haya conflictos de horario o lugar
    public Partido crear(Partido partido) {
        validarPartido(partido);
        return partidoRepository.save(partido);
    }

    // Actualiza los datos de un partido existente validando nuevamente las condiciones
    public Partido actualizar(String id, Partido partidoActualizado) {
        Partido partido = obtenerPorId(id);

        validarPartido(partidoActualizado);

        partido.setEquipoLocalId(partidoActualizado.getEquipoLocalId());
        partido.setEquipoVisitanteId(partidoActualizado.getEquipoVisitanteId());
        partido.setDatosPabellon(partidoActualizado.getDatosPabellon());
        partido.setFecha(partidoActualizado.getFecha());
        partido.setHora(partidoActualizado.getHora());
        partido.setJornada(partidoActualizado.getJornada());
        partido.setObservaciones(partidoActualizado.getObservaciones());

        return partidoRepository.save(partido);
    }

    public void eliminar(String id) {
        Partido partido = obtenerPorId(id);
        partidoRepository.delete(partido);
    }

    // Cambia el estado del partido (PROGRAMADO, EN_CURSO, FINALIZADO)
    public Partido cambiarEstado(String id, String nuevoEstado) {
        Partido partido = obtenerPorId(id);
        partido.setEstado(nuevoEstado);
        return partidoRepository.save(partido);
    }

    // Guarda las condiciones meteorológicas en el documento del partido en MongoDB
    public Partido actualizarClima(String id, CondicionesMeteorologicas clima) {
        Partido partido = obtenerPorId(id);
        partido.setCondicionesMeteorologicas(clima);
        return partidoRepository.save(partido);
    }

    // Registra el resultado final y actualiza automáticamente la clasificación de los equipos
    public Partido registrarResultado(String partidoId, ResultadoDTO dto) {
        Partido partido = obtenerPorId(partidoId);

        if (partido.getEquipoLocalPuntos() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un resultado registrado para este partido");
        }

        String equipoGanadorId;
        if (dto.getEquipoLocalPuntos() > dto.getEquipoVisitantePuntos()) {
            equipoGanadorId = partido.getEquipoLocalId();
        } else {
            equipoGanadorId = partido.getEquipoVisitanteId();
        }

        partido.setEquipoLocalPuntos(dto.getEquipoLocalPuntos());
        partido.setEquipoVisitantePuntos(dto.getEquipoVisitantePuntos());
        partido.setEquipoGanadorId(equipoGanadorId);
        partido.setEstado("FINALIZADO");

        if (dto.getObservaciones() != null) {
            partido.setObservaciones(dto.getObservaciones());
        }

        partido = partidoRepository.save(partido);

        // Actualiza victorias/derrotas de ambos equipos
        equipoService.actualizarEstadisticas(partido.getEquipoLocalId(),
                dto.getEquipoLocalPuntos() > dto.getEquipoVisitantePuntos());
        equipoService.actualizarEstadisticas(partido.getEquipoVisitanteId(),
                dto.getEquipoVisitantePuntos() > dto.getEquipoLocalPuntos());

        return partido;
    }

    // Lógica que evita que se solapen pabellones o equipos en el mismo día/hora
    private void validarPartido(Partido partido) {
        if (partido.getEquipoLocalId().equals(partido.getEquipoVisitanteId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un equipo no puede jugar contra si mismo");
        }

        if (partido.getDatosPabellon() != null && partido.getDatosPabellon().getNombre() != null) {
            boolean pabellonOcupado = partidoRepository.existsByDatosPabellonNombreAndFechaAndHora(
                    partido.getDatosPabellon().getNombre(),
                    partido.getFecha(),
                    partido.getHora());
            if (pabellonOcupado) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pabellon ya tiene un partido programado a esa hora");
            }
        }

        boolean equipoLocalOcupado = partidoRepository.existsByEquipoLocalIdAndFecha(
                partido.getEquipoLocalId(),
                partido.getFecha());
        boolean equipoVisitanteOcupado = partidoRepository.existsByEquipoVisitanteIdAndFecha(
                partido.getEquipoVisitanteId(),
                partido.getFecha());

        if (equipoLocalOcupado || equipoVisitanteOcupado) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uno de los equipos ya tiene un partido programado ese dia");
        }
    }

    // Enriquece el objeto Partido básico con nombres de equipos para el frontend
    public PartidoDTO convertirADTO(Partido partido) {
        Equipo equipoLocal = null;
        try {
            if (partido.getEquipoLocalId() != null) {
                equipoLocal = equipoService.obtenerPorId(partido.getEquipoLocalId());
            }
        } catch (Exception e) {
        }

        Equipo equipoVisitante = null;
        try {
            if (partido.getEquipoVisitanteId() != null) {
                equipoVisitante = equipoService.obtenerPorId(partido.getEquipoVisitanteId());
            }
        } catch (Exception e) {
        }

        String pabellonNombre = partido.getDatosPabellon() != null ? partido.getDatosPabellon().getNombre()
                : "Sin pabellon";
        String localNombre = equipoLocal != null ? equipoLocal.getNombre() : "Equipo Local (No encontrado)";
        String visitanteNombre = equipoVisitante != null ? equipoVisitante.getNombre()
                : "Equipo Visitante (No encontrado)";

        return new PartidoDTO(
                partido.getId(),
                localNombre,
                visitanteNombre,
                pabellonNombre,
                partido.getFecha(),
                partido.getHora(),
                partido.getJornada(),
                partido.getEstado(),
                partido.getObservaciones(),
                partido.getCondicionesMeteorologicas(),
                partido.getEquipoLocalPuntos(),
                partido.getEquipoVisitantePuntos(),
                partido.getEquipoGanadorId());
    }

    public List<PartidoDTO> obtenerTodosDTOs() {
        return obtenerTodos().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
}
