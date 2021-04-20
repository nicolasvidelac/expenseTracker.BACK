package com.group.gastos.services.Intefaces;

import com.group.gastos.models.Item;

public interface ItemService {
    Item saveItem(String username, Item newItem);
    Item updateItem(String username, String idItem, Item item);
    Item getItem(String username, String id);
    void deleteItem(String username, String idItem);
}
