package com.idgs12.profesores.profesores.services;

import com.idgs12.profesores.profesores.dto.*;
import com.idgs12.profesores.profesores.entity.ProfesorEntity;
import com.idgs12.profesores.profesores.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    // ================================
    // CREAR PROFESOR
    // ================================
    @Transactional
    public ProfesorDTO crearProfesor(CrearProfesorDTO dto) {
        System.out.println("🆕 Creando nuevo profesor: " + dto.getNumeroEmpleado());

        // Validar que no exista el número de empleado
        if (profesorRepository.existsByNumeroEmpleado(dto.getNumeroEmpleado())) {
            throw new RuntimeException("❌ Ya existe un profesor con el número de empleado: " +
                    dto.getNumeroEmpleado());
        }

        // Validar que no exista el email
        if (profesorRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("❌ Ya existe un profesor con el email: " + dto.getEmail());
        }

        // Crear entidad
        ProfesorEntity profesor = new ProfesorEntity();
        profesor.setNumeroEmpleado(dto.getNumeroEmpleado());
        profesor.setNombre(dto.getNombre());
        profesor.setApellidoPaterno(dto.getApellidoPaterno());
        profesor.setApellidoMaterno(dto.getApellidoMaterno());
        profesor.setEmail(dto.getEmail());
        profesor.setTelefono(dto.getTelefono());

        // Guardar
        ProfesorEntity guardado = profesorRepository.save(profesor);

        System.out.println("✅ Profesor creado con ID: " + guardado.getId());

        return mapearADTO(guardado);
    }

    // ================================
    // OBTENER TODOS LOS PROFESORES
    // ================================
    public List<ProfesorSimpleDTO> obtenerTodos() {
        return profesorRepository.findAll().stream()
                .map(this::mapearASimpleDTO)
                .collect(Collectors.toList());
    }

    // ================================
    // OBTENER PROFESORES ACTIVOS
    // ================================
    public List<ProfesorSimpleDTO> obtenerActivos() {
        return profesorRepository.findByActivoTrue().stream()
                .map(this::mapearASimpleDTO)
                .collect(Collectors.toList());
    }

    // ================================
    // OBTENER POR ID
    // ================================
    public ProfesorDTO obtenerPorId(Long id) {
        ProfesorEntity profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Profesor no encontrado con ID: " + id));

        return mapearADTO(profesor);
    }

    // ================================
    // OBTENER POR NÚMERO DE EMPLEADO
    // ================================
    public ProfesorDTO obtenerPorNumeroEmpleado(String numeroEmpleado) {
        ProfesorEntity profesor = profesorRepository.findByNumeroEmpleado(numeroEmpleado)
                .orElseThrow(() -> new RuntimeException(
                        "❌ Profesor no encontrado con número de empleado: " + numeroEmpleado));

        return mapearADTO(profesor);
    }

    // ================================
    // ACTUALIZAR PROFESOR
    // ================================
    @Transactional
    public ProfesorDTO actualizarProfesor(Long id, ActualizarProfesorDTO dto) {
        System.out.println("🔄 Actualizando profesor ID: " + id);

        ProfesorEntity profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Profesor no encontrado"));

        // Actualizar campos si vienen en el DTO
        if (dto.getNombre() != null) {
            profesor.setNombre(dto.getNombre());
        }
        if (dto.getApellidoPaterno() != null) {
            profesor.setApellidoPaterno(dto.getApellidoPaterno());
        }
        if (dto.getApellidoMaterno() != null) {
            profesor.setApellidoMaterno(dto.getApellidoMaterno());
        }
        if (dto.getEmail() != null) {
            // Validar que el email no esté en uso por otro profesor
            profesorRepository.findByEmail(dto.getEmail()).ifPresent(p -> {
                if (!p.getId().equals(id)) {
                    throw new RuntimeException("❌ El email ya está en uso");
                }
            });
            profesor.setEmail(dto.getEmail());
        }
        if (dto.getTelefono() != null) {
            profesor.setTelefono(dto.getTelefono());
        }

        ProfesorEntity actualizado = profesorRepository.save(profesor);

        System.out.println("✅ Profesor actualizado");

        return mapearADTO(actualizado);
    }

    // ================================
    // DESACTIVAR PROFESOR (BAJA LÓGICA)
    // ================================
    @Transactional
    public void desactivarProfesor(Long id) {
        System.out.println("❌ Desactivando profesor ID: " + id);

        ProfesorEntity profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Profesor no encontrado"));

        profesor.setActivo(false);
        profesorRepository.save(profesor);

        System.out.println("✅ Profesor desactivado");
    }

    // ================================
    // ELIMINAR PROFESOR (BAJA FÍSICA)
    // ================================
    @Transactional
    public void eliminarProfesor(Long id) {
        System.out.println("🗑️ Eliminando profesor ID: " + id);

        if (!profesorRepository.existsById(id)) {
            throw new RuntimeException("❌ Profesor no encontrado");
        }

        profesorRepository.deleteById(id);

        System.out.println("✅ Profesor eliminado permanentemente");
    }

    // ================================
    // CONTAR PROFESORES ACTIVOS
    // ================================
    public long contarActivos() {
        return profesorRepository.countByActivoTrue();
    }

    // ================================
    // MAPPERS
    // ================================
    private ProfesorDTO mapearADTO(ProfesorEntity entity) {
        ProfesorDTO dto = new ProfesorDTO();
        dto.setId(entity.getId());
        dto.setNumeroEmpleado(entity.getNumeroEmpleado());
        dto.setNombre(entity.getNombre());
        dto.setApellidoPaterno(entity.getApellidoPaterno());
        dto.setApellidoMaterno(entity.getApellidoMaterno());
        dto.setNombreCompleto(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setActivo(entity.getActivo());

        return dto;
    }

    // ================================
    // ACTIVAR PROFESOR
    // ================================
    @Transactional
    public void activarProfesor(Long id) {
        System.out.println("✅ Activando profesor ID: " + id);

        ProfesorEntity profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Profesor no encontrado"));

        profesor.setActivo(true);
        profesorRepository.save(profesor);

        System.out.println("✅ Profesor activado");
    }

    private ProfesorSimpleDTO mapearASimpleDTO(ProfesorEntity entity) {
        ProfesorSimpleDTO dto = new ProfesorSimpleDTO();
        dto.setId(entity.getId());
        dto.setNumeroEmpleado(entity.getNumeroEmpleado());
        dto.setNombreCompleto(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setActivo(entity.getActivo());
        return dto;
    }
}
