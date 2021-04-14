package com.group.gastos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Category {

    @Id
    @GeneratedValue
    private String id;

    private String description;

    public Category(String description) {
        this.description = description;
    }
}
