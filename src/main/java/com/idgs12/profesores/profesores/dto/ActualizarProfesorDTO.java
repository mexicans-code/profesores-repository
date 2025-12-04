package com.idgs12.profesores.profesores.dto;

import lombok.Data;

@Data

public class ActualizarProfesorDTO {

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    private String email;

    private String telefono;
    private boolean activo;

}