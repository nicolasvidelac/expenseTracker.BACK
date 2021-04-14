package com.group.gastos.services;

import com.group.gastos.models.Category;
import com.group.gastos.models.Item;
import com.group.gastos.repositories.CategoryRepository;
import com.group.gastos.repositories.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository _itemRepository;
    private final CategoryRepository _categoryRepository;

    public List<Item> getAllItems() {
        return _itemRepository.findAll();
    }

    public Item saveItem(Item newItem) {

        newItem.setDate(LocalDate.now());

        //si no es en cuotas, en realidad esta siendo en una cuota
        if (newItem.getCantCuotas() < 1) {
            newItem.setCantCuotas(1);
        }

        //si no viene con id
        if (newItem.getCategory().getId() == null) {

            //si viene con descripcion
            if (newItem.getCategory().getDescription() != null) {
                Category result = null;
                try {
                    //busco la categoria con esa descripcion
                    result = _categoryRepository.findAll().stream().filter(category ->
                            category.getDescription().equalsIgnoreCase(
                                    newItem.getCategory().getDescription()))
                            .findFirst().orElseThrow();
                } catch (NoSuchElementException e) {

                    //guardo la categoria que todavia no existe
                    result = _categoryRepository.save(new Category(newItem.getCategory().getDescription()));
                } finally {
                    newItem.setCategory(result);
                }

            }
            // si viene con id
        } else {
            try {
                newItem.setCategory(_categoryRepository.findAll().stream().filter(category ->
                        category.getId().equals(newItem.getCategory().getId())).findFirst().orElseThrow());
            } catch (NoSuchElementException e) {
                throw e;
            }
        }

        return _itemRepository.save(newItem);
    }

//    public Item updateItem (Item item){
//        Item entity = _itemRepository.findById(item.getId()).orElseThrow();
//
//        entity.setAmount(item.getAmount());
//        entity.setCategory(item.getCategory());
//        entity.setDate(item.getDate());
//        entity.setDescription(item.getDescription());
//
//        entity = _itemRepository.save(entity);
//        return entity;
//    }

    public void deleteItem(int id) {
        _itemRepository.delete(_itemRepository.findAll().get(id));
    }
}
