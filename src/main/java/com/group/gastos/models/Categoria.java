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
        this.descripcion = descripcion;
    }
}
