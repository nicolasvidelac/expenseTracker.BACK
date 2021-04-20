package com.group.gastos.controllers;

import com.group.gastos.models.dtos.AuthDTO;
import com.group.gastos.servicesDTO.Interfaces.UsuarioServiceDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {

    private final UsuarioServiceDTO _UsuarioServiceDTO;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthDTO authDTO) {
        try {
            if (authDTO.getPassword().isBlank() || authDTO.getUsername().isBlank()) {
                throw new IllegalStateException("missing username or password");
            }
            return ResponseEntity.ok(_UsuarioServiceDTO.login(authDTO));
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body(new ErrorMessage("Missing information", e.getCause()));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody AuthDTO authDTO) {
        try {
            if (authDTO.getPassword().isBlank() || authDTO.getUsername().isBlank() || authDTO.getNickname().isBlank()) {
                throw new IllegalStateException("missing information");
            }
            return ResponseEntity.ok(_UsuarioServiceDTO.register(authDTO));
        } catch (NullPointerException e){
            return ResponseEntity.badRequest().body(new ErrorMessage("Missing information", e.getCause()));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
        }
    }

    @GetMapping("/register/confirm{token}")
    public ResponseEntity<Object> confirmToken(@PathParam("token") String token) {
        try {
            return ResponseEntity.ok(_UsuarioServiceDTO.confirmToken(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
        }
    }


}