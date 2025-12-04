package com.idgs12.profesores.profesores.repository;

import com.idgs12.profesores.profesores.entity.ProfesorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<ProfesorEntity, Long> {

    Optional<ProfesorEntity> findByNumeroEmpleado(String numeroEmpleado);

    Optional<ProfesorEntity> findByEmail(String email);

    boolean existsByNumeroEmpleado(String numeroEmpleado);

    boolean existsByEmail(String email);

    List<ProfesorEntity> findByActivoTrue();

    List<ProfesorEntity> findByActivoFalse();

    long countByActivoTrue();
}