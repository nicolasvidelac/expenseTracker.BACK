package com.group.gastos.controllers;

import com.group.gastos.models.dtos.ItemDTO;
import com.group.gastos.others.jwt.JwtToken;
import com.group.gastos.servicesDTO.Interfaces.ItemServiceDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/items")
@AllArgsConstructor
@CrossOrigin
public class ItemController {

    private final ItemServiceDTO _itemServiceDTO;
    private final JwtToken jwtToken;

    @PostMapping
    public ResponseEntity<Object> saveItem(@RequestBody ItemDTO item, @RequestHeader(name = "Authorization") String token) {
        try {
            String username = getUsername(token);
            return ResponseEntity.ok(_itemServiceDTO.saveItem(item, username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
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
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
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
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
        }
    }

    private String getUsername(String token){
        return jwtToken.getUsername(token.substring(7));
    }
}
