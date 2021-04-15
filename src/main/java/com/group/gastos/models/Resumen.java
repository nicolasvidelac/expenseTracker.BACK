package com.group.gastos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.time.LocalDate;

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

    private Usuario usuario;
    private EstadoResumen estado;

    public Resumen(Float sueldoActual, Float valorDolar, Usuario usuario, EstadoResumen estado) {
        this.sueldoActual = sueldoActual;
        this.valorDolar = valorDolar;
        this.fechaInicio = LocalDate.now().withDayOfMonth(1);
        this.fechaFin = LocalDate.now().withDayOfMonth(1).plusDays(LocalDate.now().lengthOfMonth() - 1);
        this.nombreMes = LocalDate.now().getMonth().name();
        this.usuario = usuario;
        this.estado = estado;
    }
}
