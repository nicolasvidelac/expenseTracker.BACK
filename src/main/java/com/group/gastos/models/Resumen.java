package com.group.gastos.models;

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
        this.nombreMes = upperCaseFirst(LocalDate.now().getMonth().name());
        this.usuario_id = usuario_id;
        this.estado = estado;
        this.items = new ArrayList<>();
    }

    private static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();

        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);

        //every other element to lower case
        for(int i = 1; i<array.length ; i++){
            array[i] = Character.toLowerCase(array[i]);
        }

        // Return string.
        return new String(array);
    }
}
