package com.group.gastos.services.Intefaces;


import com.group.gastos.models.Categoria;

import java.util.List;

public interface CategoriaService {
    String getDescripcion(String id);

    List<Categoria> findAll();

    Categoria save(Categoria categoria);
}
