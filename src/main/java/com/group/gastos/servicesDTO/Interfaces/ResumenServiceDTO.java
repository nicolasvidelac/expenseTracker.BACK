package com.group.gastos.servicesDTO.Interfaces;

import com.group.gastos.models.dtos.ResumenDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ResumenServiceDTO {
    ResumenDTO findActiveResumen(String username) throws IOException, InterruptedException;
    List<ResumenDTO> findAllResumenes(String username) throws IOException, InterruptedException;
    ResumenDTO findByFechaInicio(String username, LocalDate localDate) throws IOException, InterruptedException;
    ResumenDTO updateResumen(String username, LocalDate localDate, Float nuevoMonto);
}
