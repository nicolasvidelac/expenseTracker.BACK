package com.group.gastos.servicesDTO;

import com.group.gastos.models.Categoria;
import com.group.gastos.models.Item;
import com.group.gastos.models.dtos.ItemDTO;
import com.group.gastos.services.ItemService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class ItemServiceDTO {
    private final ItemService _itemService;
    private final ModelMapper _modelMapper;

    public List<ItemDTO> getAllItems(String username){
        List<Item> items = _itemService.getAllItems(username);
        ArrayList<ItemDTO> itemsDTO = new ArrayList<>();

        for (Item item: items) {
            ItemDTO itemDTO = _modelMapper.map(item, ItemDTO.class);
            itemDTO.setCategoria_desc(upperCaseFirst(item.getCategoria().getDescripcion()));
            itemDTO.setResumen_mes(upperCaseFirst(item.getResumen().getNombreMes()));
            itemsDTO.add(itemDTO);
        }

        return itemsDTO;

    }

    public ItemDTO saveItem(ItemDTO itemDTO, String username){

        Item item = _modelMapper.map(itemDTO, Item.class);

        item.setCategoria(new Categoria());

        try {
            item.getCategoria().setDescripcion(itemDTO.getCategoria_desc());
        } catch (NullPointerException ignored){

        }

        item = _itemService.saveItem(item, username);

        itemDTO = _modelMapper.map(item, ItemDTO.class);

        itemDTO.setCategoria_desc(upperCaseFirst(item.getCategoria().getDescripcion()));
        itemDTO.setResumen_mes(upperCaseFirst(item.getResumen().getNombreMes()));

        return itemDTO;
    }

    public ItemDTO updateItem(ItemDTO itemDTO){
        return _modelMapper.map(_itemService.updateItem(_modelMapper.map(itemDTO,Item.class)), ItemDTO.class);
    }

    public ItemDTO getItem(String id){
        Item item = _itemService.getItem(id);
        ItemDTO itemDTO = _modelMapper.map( item, ItemDTO.class);

        itemDTO.setResumen_mes(upperCaseFirst(item.getResumen().getNombreMes()));
        itemDTO.setCategoria_desc(upperCaseFirst(item.getCategoria().getDescripcion()));
        return itemDTO;
    }

    public void deleteItem(String id){
         _itemService.deleteItem(id);
    }

    private static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();

        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);

        //every other element to lower case
        for(int i = 1; i<array.length ; i++){
            array[i] = Character.toLowerCase(array[i]);
        }

        // Return string.
        return new String(array);
    }
}
