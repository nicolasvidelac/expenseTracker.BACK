package com.group.gastos.services;

import com.group.gastos.models.Categoria;
import com.group.gastos.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CategoriaService {
    private final CategoryRepository _categoryRepository;

    public String getDescripcion(String id) {
        Categoria result = _categoryRepository.findAll().stream().filter(s -> s.getId().equals(id)).findFirst().orElseThrow();

        result = _categoryRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("categoria not found"));
        return (result).getDescripcion();
    }
}
