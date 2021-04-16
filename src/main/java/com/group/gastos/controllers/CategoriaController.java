package com.group.gastos.controllers;

import com.group.gastos.models.Categoria;
import com.group.gastos.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/categorias")
public class CategoriaController {

    private final CategoryRepository _categoryRepository;

    @GetMapping
    public List<Categoria> getAllCategorias() {
        try {
            return _categoryRepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping
    public Categoria getAllCategorias(@RequestBody Categoria categoria) {
        try {
            return _categoryRepository.save(categoria);
        } catch (Exception e) {
            return null;
        }
    }
}
