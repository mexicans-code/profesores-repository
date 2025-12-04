package com.idgs12.profesores.profesores.dto;

import lombok.Data;

@Data
public class ProfesorSimpleDTO {

    private Long id;
    private String numeroEmpleado;
    private String nombreCompleto;
    private String email;
    private Boolean activo;
}