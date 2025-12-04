package com.idgs12.profesores.profesores.controller;

import com.idgs12.profesores.profesores.dto.*;
import com.idgs12.profesores.profesores.services.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController

@RequestMapping("/api/profesores")
@CrossOrigin(origins = "*")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    // ================================
    // CREAR PROFESOR
    // POST /api/profesores
    // ================================
    @PostMapping
    public ResponseEntity<?> crearProfesor(@RequestBody CrearProfesorDTO dto) {
        try {
            ProfesorDTO profesor = profesorService.crearProfesor(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(profesor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", e.getMessage(),
                            "timestamp", System.currentTimeMillis()));
        }
    }

    // ================================
    // OBTENER TODOS LOS PROFESORES
    // GET /api/profesores
    // ================================
    @GetMapping
    public ResponseEntity<List<ProfesorSimpleDTO>> obtenerTodos() {
        List<ProfesorSimpleDTO> profesores = profesorService.obtenerTodos();
        return ResponseEntity.ok(profesores);
    }

    // ================================
    // OBTENER PROFESORES ACTIVOS
    // GET /api/profesores/activos
    // ================================
    @GetMapping("/activos")
    public ResponseEntity<List<ProfesorSimpleDTO>> obtenerActivos() {
        List<ProfesorSimpleDTO> profesores = profesorService.obtenerActivos();
        return ResponseEntity.ok(profesores);
    }

    // ================================
    // OBTENER POR ID
    // GET /api/profesores/{id}
    // ================================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            ProfesorDTO profesor = profesorService.obtenerPorId(id);
            return ResponseEntity.ok(profesor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "error", e.getMessage(),
                            "id", id,
                            "timestamp", System.currentTimeMillis()));
        }
    }

    // ================================
    // OBTENER POR NÚMERO DE EMPLEADO
    // GET /api/profesores/numero-empleado/{numeroEmpleado}
    // ================================
    @GetMapping("/numero-empleado/{numeroEmpleado}")
    public ResponseEntity<?> obtenerPorNumeroEmpleado(@PathVariable String numeroEmpleado) {
        try {
            ProfesorDTO profesor = profesorService.obtenerPorNumeroEmpleado(numeroEmpleado);
            return ResponseEntity.ok(profesor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "error", e.getMessage(),
                            "numeroEmpleado", numeroEmpleado,
                            "timestamp", System.currentTimeMillis()));
        }
    }

    // ================================
    // ACTUALIZAR PROFESOR
    // PUT /api/profesores/{id}
    // ================================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProfesor(
            @PathVariable Long id,
            @RequestBody ActualizarProfesorDTO dto) {
        try {
            ProfesorDTO profesor = profesorService.actualizarProfesor(id, dto);
            return ResponseEntity.ok(profesor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", e.getMessage(),
                            "id", id,
                            "timestamp", System.currentTimeMillis()));
        }
    }

    // ================================
    // DESACTIVAR PROFESOR (BAJA LÓGICA)
    // PATCH /api/profesores/{id}/desactivar
    // ================================
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivarProfesor(@PathVariable Long id) {
        try {
            profesorService.desactivarProfesor(id);
            return ResponseEntity.ok(
                    Map.of(
                            "mensaje", "Profesor desactivado correctamente",
                            "id", id,
                            "timestamp", System.currentTimeMillis()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", e.getMessage(),
                            "id", id,
                            "timestamp", System.currentTimeMillis()));
        }
    }

    // ================================
    // ACTIVAR PROFESOR
    // PATCH /api/profesores/{id}/activar
    // ================================
    @PatchMapping("/{id}/activar")
    public ResponseEntity<?> activarProfesor(@PathVariable Long id) {
        try {
            profesorService.activarProfesor(id);

            return ResponseEntity.ok(
                    Map.of(
                            "mensaje", "Profesor activado correctamente",
                            "id", id,
                            "timestamp", System.currentTimeMillis()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", e.getMessage(),
                            "id", id,
                            "timestamp", System.currentTimeMillis()));
        }
    }

    // ================================
    // ELIMINAR PROFESOR (BAJA FÍSICA)
    // DELETE /api/profesores/{id}
    // ================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProfesor(@PathVariable Long id) {
        try {
            profesorService.eliminarProfesor(id);
            return ResponseEntity.ok(
                    Map.of(
                            "mensaje", "Profesor eliminado permanentemente",
                            "id", id,
                            "timestamp", System.currentTimeMillis()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", e.getMessage(),
                            "id", id,
                            "timestamp", System.currentTimeMillis()));
        }
    }

    // ================================
    // CONTAR PROFESORES ACTIVOS
    // GET /api/profesores/estadisticas/activos
    // ================================
    @GetMapping("/estadisticas/activos")
    public ResponseEntity<Map<String, Object>> contarActivos() {
        long total = profesorService.contarActivos();
        return ResponseEntity.ok(
                Map.of(
                        "totalActivos", total,
                        "timestamp", System.currentTimeMillis()));
    }

    // ================================
    // HEALTH CHECK
    // GET /api/profesores/health
    // ================================
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("servicio", "Microservicio de Profesores");
        response.put("version", "1.0.0");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
}