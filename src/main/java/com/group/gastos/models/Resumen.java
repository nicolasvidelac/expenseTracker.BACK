package com.group.gastos.models;

import com.group.gastos.others.enums.MesesEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Resumen {

    @Id
    @GeneratedValue
    private String id;

    private Float sueldoActual = 0F;
    private Float valorDolar = 0F;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String nombreMes = "";
    private Float totalGasto = 0F;

    private String usuario_id;
    private EstadoResumen estado;

    private ArrayList<Item> items;

    public Resumen(Float sueldoActual, Float valorDolar, String usuario_id, EstadoResumen estado) {
        this.sueldoActual = sueldoActual;
        this.valorDolar = valorDolar;
        this.fechaInicio = LocalDate.now().withDayOfMonth(1);
        this.fechaFin = LocalDate.now().withDayOfMonth(1).plusDays(LocalDate.now().lengthOfMonth() - 1);
        this.nombreMes = MesesEnum.valueOf(LocalDate.now().getMonth().name()).getLabel();
        this.usuario_id = usuario_id;
        this.estado = estado;
        this.items = new ArrayList<>();
    }
}
