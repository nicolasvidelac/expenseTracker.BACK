package com.group.gastos.controllers;

import com.group.gastos.models.dtos.UsuarioDTO;
import com.group.gastos.others.jwt.JwtToken;
import com.group.gastos.servicesDTO.UsuarioServiceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuario")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioServiceDTO _usuarioServiceDTO;
    private final JwtToken jwtToken;

    @PatchMapping
    public ResponseEntity<Object> updateUsuario(@RequestHeader("Authorization") String token,
                                                @RequestBody UsuarioDTO usuarioDTO) {
        try {
            String username = getUsername(token);
            return ResponseEntity.ok(_usuarioServiceDTO.updateUser(username, usuarioDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getUsuario(@RequestHeader("Authorization") String token) {
        try {
            String username = getUsername(token);
            return ResponseEntity.ok(_usuarioServiceDTO.getUsuario(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUsuario(@RequestHeader("Authorization") String token) {
        try {
            String username = getUsername(token);
            _usuarioServiceDTO.deleteUsuario(username);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String getUsername(String token){
        return jwtToken.getUsername(token.substring(7));
    }
}