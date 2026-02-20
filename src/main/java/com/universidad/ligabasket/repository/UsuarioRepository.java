package com.universidad.ligabasket.repository;

import com.universidad.ligabasket.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRol(String rol);

    List<Usuario> findByEquipoId(String equipoId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Queries para jugadores (usuarios con rol JUGADOR)
    List<Usuario> findByEquipoIdAndRol(String equipoId, String rol);

    List<Usuario> findByPosicion(String posicion);

    boolean existsByEquipoIdAndDorsal(String equipoId, Integer dorsal);

    long countByEquipoIdAndRol(String equipoId, String rol);
}
