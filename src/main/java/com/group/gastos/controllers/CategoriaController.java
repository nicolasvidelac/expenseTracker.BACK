package com.group.gastos.controllers;

import com.group.gastos.models.Categoria;
import com.group.gastos.services.Intefaces.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        try {
            return ResponseEntity.ok(categoriaService.findAll());
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping
    public Categoria saveCategoria(@RequestBody Categoria categoria) {
        try {
            return categoriaService.save(categoria);
        } catch (Exception e) {
            return null;
        }
    }
}
