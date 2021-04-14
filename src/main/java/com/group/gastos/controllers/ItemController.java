package com.group.gastos.controllers;

import com.group.gastos.models.Item;
import com.group.gastos.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    @Autowired
    private ItemService _itemService;

    @GetMapping
    public List<Item> getAllItems() {
        return _itemService.getAllItems();
    }

    @PostMapping
    public Item saveItem(@RequestBody Item item) {
        return _itemService.saveItem(item);
    }
}
