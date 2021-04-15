package com.group.gastos.repositories;

import com.group.gastos.models.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Categoria, String> {
    Optional<Categoria> findByDescripcion(String s);
}
