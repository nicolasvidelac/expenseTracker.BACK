package com.group.gastos.services;

import com.group.gastos.models.Item;
import com.group.gastos.models.Resumen;
import com.group.gastos.others.others.ResumenUtils;
import com.group.gastos.repositories.CategoriaRepository;
import com.group.gastos.repositories.ResumenRepository;
import com.group.gastos.repositories.UsuarioRepository;
import com.group.gastos.services.Intefaces.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ItemServiceImp implements ItemService {

    private ResumenRepository _resumenRepository;
    private UsuarioRepository _usuarioRepository;
    private final CategoriaRepository _categoriaRepository;
    private ResumenUtils resumenUtils;

    @Autowired
    public ItemServiceImp(ResumenRepository _resumenRepository,
                          UsuarioRepository _usuarioRepository,
                          CategoriaRepository _categoriaRepository,
                          ResumenUtils resumenUtils) {

        this._resumenRepository = _resumenRepository;
        this._usuarioRepository = _usuarioRepository;
        this._categoriaRepository = _categoriaRepository;
        this.resumenUtils = resumenUtils;
    }

    public Item saveItem(String username, Item newItem) {

        Resumen resumen = checkItem(username, newItem);

        resumen.getItems().add(newItem);
        resumen.setTotalGasto(resumenUtils.calculateGastoTotal(resumen));
        _resumenRepository.save(resumen);

        return newItem;
    }

    public Item updateItem(String username, String idItem, Item newItem) {

        Resumen resumen = checkItem(username, newItem);

        //todo si lo cambio de un mes a otro, explota
        resumen.getItems().stream().filter(s -> s.getId().equals(idItem)).findFirst()
                .ifPresentOrElse(s -> s = newItem, () -> {
                    throw new RuntimeException("item doesn't exist for this period");
                });

        resumen.setTotalGasto(resumenUtils.calculateGastoTotal(resumen));
        resumen = _resumenRepository.save(resumen);

        return resumen.getItems().stream().filter(s -> s.getId().equals(idItem)).findFirst().orElseThrow();
    }

    public Item getItem(String username, String id) {
        String idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();

        List<Resumen> resumenes = _resumenRepository.findAll().stream().filter(s ->
                s.getUsuario_id().equals(idUsuario))
                .collect(Collectors.toList());

        //todo averiguar que chota es AtomicReference
        AtomicReference<Item> result = new AtomicReference<>();

        for (Resumen resumen : resumenes
        ) {
            resumen.getItems().stream().filter(
                    s -> s.getId().equals(id))
                    .findFirst().ifPresent(result::set);

            //si el if esta adentro del for, va a salir apenas encuentre el item, si no, tengo que esperar
            // a que termine de revisar todos los resumenes al pedo
            if (result.get() != null) {
                return result.get();
            }
        }
        throw new NoSuchElementException("Item not found");
    }

    public void deleteItem(String username, String idItem) {

        String idUsuario;
        try {
            idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();
        } catch (Exception e) {
            throw new NoSuchElementException("User not found");
        }

        List<Resumen> resumenes = _resumenRepository.findAll().stream().filter(s ->
                s.getUsuario_id().equals(idUsuario))
                .collect(Collectors.toList());

        boolean result;
        for (Resumen resumen : resumenes
        ) {
            result = resumen.getItems().removeIf(s -> s.getId().equals(idItem));

            //si el if esta adentro del for, va a salir apenas encuentre el item, si no, tengo que esperar
            // a que termine de revisar todos los resumenes al pedo
            if (result) {
                resumen.setTotalGasto(resumenUtils.calculateGastoTotal(resumen));
                _resumenRepository.save(resumen);
                return;
            }
        }
        throw new NoSuchElementException("Item not found");
    }

    private Resumen checkItem(String username, Item newItem) {
        try {
            if (newItem.getMonto().isNaN() ||
                    newItem.getDescripcion().isBlank() ||
                    newItem.getCategoria_id().isBlank()) {

                throw new IllegalArgumentException("Invalid item attributes");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid item attributes");
        }

        try {
            _categoriaRepository.findById(newItem.getCategoria_id()).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Category not found");
        }

        try {
            if (newItem.getFecha() == null) {
                newItem.setFecha(LocalDate.now());
            }
        } catch (NullPointerException e) {
            newItem.setFecha(LocalDate.now());
        }

        String idUsuario;
        try {
            idUsuario = (_usuarioRepository.findByUsername(username).stream().findFirst().orElseThrow()).getId();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }


        //si no es en cuotas, en realidad esta siendo en una cuota
        try {
            newItem.setCuotasPendientes(newItem.getCuotasTotal() - 1);
        } catch (NullPointerException e) {
            newItem.setCuotasTotal(1);
            newItem.setCuotasPendientes(newItem.getCuotasTotal() - 1);
        }

        //todo si no encuentra un resumen para ese periodo, deberia hacerlo
        Resumen resumen;
        try {
            resumen = _resumenRepository.findAll().stream().filter(
                    s ->
                            s.getUsuario_id().equals(idUsuario) &&
                                    s.getFechaInicio().isBefore(newItem.getFecha()) &&
                                    s.getFechaFin().isAfter(newItem.getFecha())

            ).findFirst().orElseThrow();

        } catch (Exception e) {
            throw new NoSuchElementException("Resumen not found");
        }

        return resumen;
    }
}