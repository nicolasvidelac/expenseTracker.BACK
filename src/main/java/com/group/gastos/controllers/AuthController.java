package com.group.gastos.controllers;

import com.group.gastos.models.Usuario;
import com.group.gastos.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegistrationService _registrationService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Usuario user) throws ChangeSetPersister.NotFoundException {
        try {
            return ResponseEntity.ok(_registrationService.login(user));
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Usuario newUser) {
        try {
            return ResponseEntity.ok(_registrationService.register(newUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/register/confirm{token}")
    public ResponseEntity<Object> confirmToken(@PathParam("token") String token) {
        try {
            return ResponseEntity.ok(_registrationService.confirmToken(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}