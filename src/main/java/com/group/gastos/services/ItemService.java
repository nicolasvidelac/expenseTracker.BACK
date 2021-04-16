package com.group.gastos.services;

import com.group.gastos.models.EstadoResumen;
import com.group.gastos.models.Item;
import com.group.gastos.models.Resumen;
import com.group.gastos.others.ResumenUtils;
import com.group.gastos.repositories.CategoryRepository;
import com.group.gastos.repositories.ResumenRepository;
import com.group.gastos.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private CategoryRepository _categoryRepository;
    @Autowired
    private ResumenRepository _resumenRepository;
    @Autowired
    private UsuarioRepository _usuarioRepository;
    @Autowired
    private ResumenUtils resumenUtils;
    @Autowired
    @Qualifier("getEstadoActivo")
    private EstadoResumen _estadoResumenActivo;

    public Item saveItem(String username, Item newItem) {

        try {
            if (newItem.getFecha() == null) {
                newItem.setFecha(LocalDate.now());
            }
        } catch (NullPointerException e) {
            newItem.setFecha(LocalDate.now());
        }

        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();

        //si no es en cuotas, en realidad esta siendo en una cuota
        try {
            newItem.setCuotasPendientes(newItem.getCuotasTotal() - 1);
        } catch (NullPointerException e) {
            newItem.setCuotasTotal(1);
            newItem.setCuotasPendientes(newItem.getCuotasTotal() - 1);
        }

        Resumen resumen = _resumenRepository.findAll().stream().filter(
                s ->
                        s.getUsuario_id().equals(idUsuario) &&
                                s.getFechaInicio().isBefore(newItem.getFecha()) &&
                                s.getFechaFin().isAfter(newItem.getFecha())

        ).findFirst()

                .orElseThrow(
                        () -> new NoSuchElementException("resumen not found")
                );

        resumen.getItems().add(newItem);
        resumenUtils.calculateGastoTotal(resumen);
        _resumenRepository.save(resumen);

        return newItem;
    }


    public Item updateItem(String username, String idItem, Item item) {

        Item finalItem = item;

        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();

        Resumen resumen = _resumenRepository.findAll().stream().filter(s ->
                s.getUsuario_id().equals(idUsuario) &&
                        s.getFechaInicio().isBefore(item.getFecha()) &&
                        s.getFechaFin().isAfter(item.getFecha())
        ).findFirst().orElseThrow();

        resumen.getItems().stream().filter(s -> s.getId().equals(idItem)).findFirst()
                .ifPresentOrElse(s -> s = finalItem, (Runnable) new IllegalStateException());

        resumenUtils.calculateGastoTotal(resumen);
        resumen = _resumenRepository.save(resumen);

        return resumen.getItems().stream().filter(s -> s.getId().equals(idItem)).findFirst().orElseThrow();
    }

    public Item getItem(String username, String id) {
        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();

        List<Resumen> resumenes = _resumenRepository.findAll().stream().filter(s -> s.getUsuario_id().equals(idUsuario)).collect(Collectors.toList());

        for (Resumen resumen : resumenes
        ) {
            return resumen.getItems().stream().filter(s -> s.getId().equals(id)).findFirst().orElseThrow();
        }
        return null;
    }

    public void deleteItem(String username, String idItem) {

        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();

        List<Resumen> resumenes = _resumenRepository.findAll().stream().filter(s -> s.getUsuario_id().equals(idUsuario)).collect(Collectors.toList());

        for (Resumen resumen : resumenes
        ) {
            boolean result;
            result = resumen.getItems().removeIf(s -> s.getId().equals(idItem));

            if (result) {
                resumenUtils.calculateGastoTotal(resumen);
                _resumenRepository.save(resumen);
                return;
            }
        }
        throw new NoSuchElementException("item not found");
    }
}
