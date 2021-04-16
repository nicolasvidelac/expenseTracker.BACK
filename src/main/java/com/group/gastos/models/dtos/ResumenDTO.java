package com.group.gastos.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@NoArgsConstructor
@Data
public class ResumenDTO {

    private Float sueldoActual;
    private Float valorDolar;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String nombreMes;
    private Float totalGasto;
    private ArrayList<ItemDTO> items;

}
