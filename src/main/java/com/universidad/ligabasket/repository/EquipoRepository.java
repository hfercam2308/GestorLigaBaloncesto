package com.universidad.ligabasket.repository;

import com.universidad.ligabasket.model.Equipo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipoRepository extends MongoRepository<Equipo, String> {

    Optional<Equipo> findByNombre(String nombre);

    List<Equipo> findByUniversidad(String universidad);

    List<Equipo> findByCiudad(String ciudad);

    List<Equipo> findAllByOrderByPuntosDesc();
}