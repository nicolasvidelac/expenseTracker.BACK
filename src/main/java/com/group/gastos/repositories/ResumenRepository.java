package com.group.gastos.repositories;

import com.group.gastos.models.Resumen;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResumenRepository extends MongoRepository<Resumen, String> {
}
