package com.group.gastos.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoriaDTO {
    private String descripcion;
    private Boolean estado;
}
