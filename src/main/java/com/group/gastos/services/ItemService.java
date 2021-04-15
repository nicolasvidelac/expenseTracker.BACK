package com.group.gastos.services;

import com.group.gastos.models.Categoria;
import com.group.gastos.models.EstadoResumen;
import com.group.gastos.models.Item;
import com.group.gastos.models.Resumen;
import com.group.gastos.repositories.CategoryRepository;
import com.group.gastos.repositories.ItemRepository;
import com.group.gastos.repositories.ResumenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository _itemRepository;
    private final CategoryRepository _categoryRepository;
    private final ResumenRepository _resumenRepository;

    @Qualifier("getEstadoActivo")
    private final EstadoResumen _estadoResumenActivo;

    public List<Item> getAllItems(String username) {
        return _itemRepository.findAll().stream().filter(item ->
                    item.getResumen().getUsuario().getUsername().equals(username) &&
                    item.getResumen().getEstado().equals(_estadoResumenActivo) &&
                    item.getFecha().isAfter(LocalDate.now().minusMonths(1)))
                .collect(Collectors.toList());
    }

    public Item saveItem(Item newItem, String username) {

        newItem.setFecha(LocalDate.now());

        //si no es en cuotas, en realidad esta siendo en una cuota
        try{
            newItem.setCuotasPendientes(newItem.getCuotasTotal() - 1);
        } catch (NullPointerException e){
            newItem.setCuotasTotal(1);
            newItem.setCuotasPendientes(newItem.getCuotasTotal() - 1);
        }

        newItem.setCategoria(handleCategoria(newItem.getCategoria().getDescripcion()));

        Resumen resumen = _resumenRepository.findAll().stream().filter(
                s -> s.getUsuario().getUsername().equals(username) &&
                        s.getEstado().equals(_estadoResumenActivo)
        ).findFirst().orElseThrow(
                () -> new NoSuchElementException("resumen not found")
        );

        newItem.setResumen(resumen);


        return _itemRepository.save(newItem);
    }

    private Categoria handleCategoria(String descripcion){

            Categoria result = null;

            try {
                //busco la categoria con esa descripcion
                result = _categoryRepository.findByDescripcion(descripcion.toLowerCase(Locale.ROOT)).orElseThrow();

            } catch (NoSuchElementException e) {
                //guardo la categoria que todavia no existe
                result = _categoryRepository.save(new Categoria(descripcion.toLowerCase(Locale.ROOT)));
            }

            return result;

    }

    public Item updateItem (Item item) {

        Item entity = _itemRepository.findById(item.getId()).orElseThrow(() ->
                new NoSuchElementException("item not found")
        );

        item.setCategoria(handleCategoria(item.getCategoria().getDescripcion()));
        item.setId(entity.getId());
        item = _itemRepository.save(item);
        return item;
    }

    public Item getItem(String id){
        return _itemRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("item doesn't exist")
        );
    }

    public void deleteItem(String id) {

        _itemRepository.delete(_itemRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("item doesn't exist")
        ));

    }
}
