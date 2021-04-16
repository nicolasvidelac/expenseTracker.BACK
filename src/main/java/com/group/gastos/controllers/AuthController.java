package com.group.gastos.controllers;

import com.group.gastos.models.dtos.AuthDTO;
import com.group.gastos.servicesDTO.UsuarioServiceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioServiceDTO _UsuarioServiceDTO;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthDTO authDTO) {
        try {
            if (authDTO.getPassword().isBlank() || authDTO.getUsername().isBlank()) {
                throw new IllegalStateException("missing username or password");
            }
            return ResponseEntity.ok(_UsuarioServiceDTO.login(authDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthDTO authDTO) {
        try {
            if (authDTO.getPassword().isBlank() || authDTO.getUsername().isBlank()) {
                throw new IllegalStateException("missing username or password");
            }
            return ResponseEntity.ok(_UsuarioServiceDTO.register(authDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/register/confirm{token}")
    public ResponseEntity<Object> confirmToken(@PathParam("token") String token) {
        try {
            return ResponseEntity.ok(_UsuarioServiceDTO.confirmToken(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}