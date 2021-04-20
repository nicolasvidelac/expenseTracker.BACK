package com.group.gastos.servicesDTO.Interfaces;

import com.group.gastos.models.dtos.ItemDTO;

public interface ItemServiceDTO {
    ItemDTO saveItem(ItemDTO itemDTO, String username);

    ItemDTO updateItem(String username, String idItem, ItemDTO itemDTO);

    ItemDTO getItem(String username, String idItem);

    void deleteItem(String username, String idItem);
}