package com.group.gastos.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ItemDTO {
    private String descripcion;
    private LocalDate fecha;
    private Float monto;
    private Boolean isPesos = true;
    private Integer cuotasTotal;
    private Integer cuotasPendientes;

    private String categoria_desc;
    private String resumen_mes;
}
