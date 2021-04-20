package com.group.gastos.services.Intefaces;

import com.group.gastos.models.Resumen;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ResumenService {
    Resumen findActiveResumen(String username) throws IOException, InterruptedException;
    List<Resumen> findAllResumenes(String username);
    Resumen createResumen(String username) throws IOException, InterruptedException;
    Resumen createResumen(String username, Float sueldo) throws InterruptedException, IOException;
    Resumen findResumenByFechaInicio(LocalDate localDate, String username);
    Resumen updateResumen(String username, LocalDate localDate, Float nuevoMonto);
    void setResumenInactive() throws IOException, InterruptedException;
    void createNewResumenes()throws IOException;
}
