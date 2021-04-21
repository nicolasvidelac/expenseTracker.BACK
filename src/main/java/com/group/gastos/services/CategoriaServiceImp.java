package com.group.gastos.services;

import com.group.gastos.models.Categoria;
import com.group.gastos.repositories.CategoriaRepository;
import com.group.gastos.services.Intefaces.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CategoriaServiceImp implements CategoriaService {
    private final CategoriaRepository _categoryRepository;

    public String getDescripcion(String id) {
        Categoria result;

        result = _categoryRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Category not found"));

        return (result).getDescripcion();
    }

    public List<Categoria> findAll() {
        return _categoryRepository.findAll();
    }

    @Override
    public Categoria save(Categoria categoria) {
        if (categoria.getDescripcion().isBlank()) {
            throw new DataIntegrityViolationException("descripcion cannot be empty");
        }
        return _categoryRepository.save(categoria);
    }
}
