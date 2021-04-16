package com.group.gastos.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Item {

    private String id = RandomString.make(30);

    private String descripcion;
    private LocalDate fecha;
    private Float monto;
    private Boolean isPesos;
    private Integer cuotasTotal;
    private Integer cuotasPendientes;

    private String categoria_id;

}
