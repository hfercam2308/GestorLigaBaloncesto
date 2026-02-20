package com.universidad.ligabasket.service;

import com.universidad.ligabasket.model.Usuario;
import com.universidad.ligabasket.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Lógica de negocio para la gestión de usuarios y jugadores
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado: " + username));
    }

    public List<Usuario> obtenerPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    public List<Usuario> obtenerPorEquipo(String equipoId) {
        return usuarioRepository.findByEquipoId(equipoId);
    }

    public Usuario actualizar(String id, Usuario usuarioActualizado) {
        Usuario usuario = obtenerPorId(id);

        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setApellidos(usuarioActualizado.getApellidos());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setEquipoId(usuarioActualizado.getEquipoId());
        usuario.setActivo(usuarioActualizado.getActivo());

        return usuarioRepository.save(usuario);
    }

    public void eliminar(String id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepository.delete(usuario);
    }

    public void asignarEquipo(String usuarioId, String equipoId) {
        Usuario usuario = obtenerPorId(usuarioId);
        usuario.setEquipoId(equipoId);
        usuarioRepository.save(usuario);
    }

    // --- Metodos de jugador (usuarios con rol JUGADOR) ---

    public List<Usuario> obtenerJugadores() {
        return usuarioRepository.findByRol("JUGADOR");
    }

    public List<Usuario> obtenerJugadoresPorEquipo(String equipoId) {
        return usuarioRepository.findByEquipoIdAndRol(equipoId, "JUGADOR");
    }

    public List<Usuario> obtenerJugadoresPorPosicion(String posicion) {
        return usuarioRepository.findByPosicion(posicion);
    }

    // Lógica compleja: valida dorsal único en el equipo y límite de 15 jugadores
    // por plantilla
    public Usuario actualizarDatosJugador(String id, Usuario datosJugador) {
        Usuario usuario = obtenerPorId(id);

        if (!"JUGADOR".equals(usuario.getRol())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden actualizar datos de jugador para usuarios con rol JUGADOR");
        }

        // El dorsal debe ser único dentro de cada equipo
        if (datosJugador.getDorsal() != null && usuario.getEquipoId() != null) {
            if (!datosJugador.getDorsal().equals(usuario.getDorsal()) &&
                    usuarioRepository.existsByEquipoIdAndDorsal(usuario.getEquipoId(), datosJugador.getDorsal())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un jugador con ese dorsal en el equipo");
            }
        }

        // Límite reglamentario de jugadores por equipo (máximo 15)
        if (datosJugador.getEquipoId() != null && !datosJugador.getEquipoId().equals(usuario.getEquipoId())) {
            long cantidadJugadores = usuarioRepository.countByEquipoIdAndRol(datosJugador.getEquipoId(), "JUGADOR");
            if (cantidadJugadores >= 15) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El equipo ya tiene el maximo de jugadores permitidos (15)");
            }
            usuario.setEquipoId(datosJugador.getEquipoId());
        }

        usuario.setDorsal(datosJugador.getDorsal());
        usuario.setPosicion(datosJugador.getPosicion());
        usuario.setAltura(datosJugador.getAltura());
        usuario.setPeso(datosJugador.getPeso());
        usuario.setFechaNacimiento(datosJugador.getFechaNacimiento());
        usuario.setNacionalidad(datosJugador.getNacionalidad());
        usuario.setAnoIngreso(datosJugador.getAnoIngreso());

        return usuarioRepository.save(usuario);
    }
}
