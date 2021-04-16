package com.group.gastos.servicesDTO;

import com.group.gastos.models.Item;
import com.group.gastos.models.dtos.ItemDTO;
import com.group.gastos.services.CategoriaService;
import com.group.gastos.services.ItemService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ItemServiceDTO {
    private final ItemService _itemService;
    private final ModelMapper _modelMapper;
    private final CategoriaService _categoriaService;

    public ItemDTO saveItem(ItemDTO itemDTO, String username) {

        Item item = _modelMapper.map(itemDTO, Item.class);

        item = _itemService.saveItem(username, item);

        itemDTO = _modelMapper.map(item, ItemDTO.class);

        itemDTO.setCategoria_desc(_categoriaService.getDescripcion(item.getCategoria_id()));

        return itemDTO;
    }

    public ItemDTO updateItem(String username, String idItem, ItemDTO itemDTO) {
        return _modelMapper.map(_itemService.updateItem(username, idItem, _modelMapper.map(itemDTO, Item.class)), ItemDTO.class);
    }

    public ItemDTO getItem(String username, String idItem) {
        Item item = _itemService.getItem(username, idItem);
        ItemDTO itemDTO = _modelMapper.map(item, ItemDTO.class);

        itemDTO.setCategoria_desc(_categoriaService.getDescripcion(itemDTO.getCategoria_id()));
        return itemDTO;
    }

    public void deleteItem(String username, String idItem) {
        _itemService.deleteItem(username, idItem);
    }
}
