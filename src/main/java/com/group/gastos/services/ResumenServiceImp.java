package com.group.gastos.services;

import com.group.gastos.models.EstadoResumen;
import com.group.gastos.models.Resumen;
import com.group.gastos.others.others.ResumenUtils;
import com.group.gastos.repositories.ResumenRepository;
import com.group.gastos.repositories.UsuarioRepository;
import com.group.gastos.services.Intefaces.ResumenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ResumenServiceImp implements ResumenService {

    @Autowired
    private UsuarioRepository _usuarioRepository;

    @Autowired
    private ResumenRepository _resumenRepository;

    @Autowired
    private ResumenUtils resumenUtils;

    @Autowired
    @Qualifier("getEstadoActivo")
    private EstadoResumen estadoActivo;

    @Autowired
    @Qualifier("getEstadoInactivo")
    private EstadoResumen estadoInactivo;

    public Resumen findActiveResumen(String username) throws IOException, InterruptedException {
        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();
        Resumen resumen = _resumenRepository.findAll().stream().filter(s -> s.getEstado().equals(estadoActivo) &&
                s.getUsuario_id().equals(idUsuario))
                .findFirst().orElseThrow(() -> new NoSuchElementException("resumen no encontrado"));

        resumen.setValorDolar(resumenUtils.getPrecioDolar());
        return _resumenRepository.save(resumen);
    }

    public List<Resumen> findAllResumenes(String username) {
        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();
        List<Resumen> resumenes = _resumenRepository.findAll().stream().filter(s ->
                    s.getUsuario_id().equals(idUsuario))
                .collect(Collectors.toList());

        return resumenes;
    }

    public Resumen createResumen(String username) throws IOException, InterruptedException {
        return createResumen(username, 0F);
    }

    public Resumen createResumen(String username, Float sueldo) throws InterruptedException, IOException {
        Float valorDolar = resumenUtils.getPrecioDolar();
        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();

        Resumen resumen = (new Resumen(sueldo, valorDolar, idUsuario, estadoActivo));
        return saveResumen(resumen);
    }

    public Resumen findResumenByFechaInicio(LocalDate localDate, String username) {
        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();
        Resumen resumen = _resumenRepository.findAll().stream().filter(s ->
                        s.getUsuario_id().equals(idUsuario) &&
                        s.getFechaInicio().getMonth().equals(localDate.getMonth()) &&
                        s.getFechaInicio().getYear() == localDate.getYear())
                .findFirst().orElseThrow(
                        () -> new NoSuchElementException("resumen no encontrado")
                );

        return resumen;
    }

    public Resumen updateResumen(String username, LocalDate localDate, Float nuevoMonto){
        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();
        Resumen originalResumen = _resumenRepository.findAll().stream().filter(s -> s.getUsuario_id().equals(idUsuario) &&
                s.getFechaInicio().getMonth().equals(localDate.getMonth()) &&
                s.getFechaInicio().getYear() == localDate.getYear())
            .findFirst().orElseThrow(
                    () -> new NoSuchElementException("resumen no encontrado")
                );

        if (nuevoMonto > 0F){
            originalResumen.setSueldoActual(nuevoMonto);
        }
        return _resumenRepository.save(originalResumen);
    }

    public void setResumenInactive() throws IOException, InterruptedException {
        //todo por ahi deberia traer solo el primero
        List<Resumen> resumenList = _resumenRepository.findAll().stream().filter(s ->
                s.getEstado().equals(estadoActivo))
                .collect(Collectors.toList());

        for(Resumen resumen: resumenList){
            resumen.setEstado(estadoInactivo);
            resumen.setValorDolar(resumenUtils.getPrecioDolar());
            resumenUtils.calculateGastoTotal(resumen);
            _resumenRepository.save(resumen);
        }
    }

    public void createNewResumenes()throws IOException {
        HashMap<String, Float> usuarios = new HashMap<>();
        _usuarioRepository.findAll().forEach( s ->
                usuarios.put(s.getUsername(), s.getSueldo())
        );

        usuarios.forEach((k,v) -> {
            try {
                createResumen(k,v);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private Resumen saveResumen(Resumen resumen) {
        return _resumenRepository.save(resumen);
    }

}
