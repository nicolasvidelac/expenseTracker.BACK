package com.group.gastos.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;

@Data
@Document
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue
    private String id;
    private String descripcion;

    public Categoria(String descripcion) {
        this.descripcion = upperCaseFirst(descripcion);
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
