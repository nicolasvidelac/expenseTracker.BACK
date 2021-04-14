package com.group.gastos.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Document
public class Item {

    @Id
    @GeneratedValue
    private String id;

    private String description;
    private LocalDate date;
    private Float amount;

    private Category category;
    private int cantCuotas;

}
