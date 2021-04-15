package com.group.gastos.controllers;

import com.group.gastos.models.dtos.ItemDTO;
import com.group.gastos.others.jwt.JwtToken;
import com.group.gastos.servicesDTO.ItemServiceDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@AllArgsConstructor
public class ItemController {

    private ItemServiceDTO _itemServiceDTO;
    private JwtToken jwtToken;


    @GetMapping
    public List<ItemDTO> getAllItems(@RequestHeader(name = "Authorization") String token) {

        String username = jwtToken.getUsername(token.substring(7));

        return _itemServiceDTO.getAllItems(username);
    }

    @PostMapping
    public ItemDTO saveItem(@RequestBody ItemDTO item, @RequestHeader(name = "Authorization") String token) {
        String username = jwtToken.getUsername(token.substring(7));
        return _itemServiceDTO.saveItem(item, username);
    }

    @GetMapping("/{id}")
    public ItemDTO getItem(@PathParam("id") String idItem){
        return _itemServiceDTO.getItem(idItem);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathParam("id") String id){
        _itemServiceDTO.deleteItem(id);
    }
}
