package com.universidad.ligabasket.repository;

import com.universidad.ligabasket.model.Partido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Interfaz para el acceso a datos de Partidos en MongoDB
@Repository
public interface PartidoRepository extends MongoRepository<Partido, String> {

    List<Partido> findByJornada(Integer jornada);

    List<Partido> findByEstado(String estado);

    List<Partido> findByFecha(LocalDate fecha);

    // Consulta personalizada para buscar partidos donde el equipo sea Local o
    // Visitante
    @Query("{ $or: [ { 'equipoLocalId': ?0 }, { 'equipoVisitanteId': ?0 } ] }")
    List<Partido> findByEquipoId(String equipoId);

    // Métodos para verificar disponibilidad de pabellones y equipos
    boolean existsByDatosPabellonNombreAndFechaAndHora(String pabellonNombre, LocalDate fecha, String hora);

    boolean existsByEquipoLocalIdAndFecha(String equipoLocalId, LocalDate fecha);

    boolean existsByEquipoVisitanteIdAndFecha(String equipoVisitanteId, LocalDate fecha);

    List<Partido> findAllByOrderByFechaAscHoraAsc();
}
