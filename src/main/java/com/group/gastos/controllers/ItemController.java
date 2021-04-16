package com.group.gastos.controllers;

import com.group.gastos.models.dtos.ItemDTO;
import com.group.gastos.others.jwt.JwtToken;
import com.group.gastos.servicesDTO.ItemServiceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
@AllArgsConstructor
public class ItemController {

    private ItemServiceDTO _itemServiceDTO;
    private JwtToken jwtToken;

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestBody ItemDTO item, @RequestHeader(name = "Authorization") String token) {
        try {
            String username = getUsername(token);
            return ResponseEntity.ok(_itemServiceDTO.saveItem(item, username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<Object> getItem(@PathVariable("idItem") String idItem,
                                          @RequestHeader(name = "Authorization") String token

    ) {
        try {
            String username = getUsername(token);
            return ResponseEntity.ok(_itemServiceDTO.getItem(username, idItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{idItem}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(name = "Authorization") String token,
                                             @PathVariable("idItem") String idItem) {
        try {
            String username = getUsername(token);
            _itemServiceDTO.deleteItem(username, idItem);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String getUsername(String token){
        return jwtToken.getUsername(token.substring(7));
    }
}
