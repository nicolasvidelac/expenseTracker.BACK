package com.group.gastos.controllers;

import com.group.gastos.models.Usuario;
import com.group.gastos.others.jwt.JwtToken;
import com.group.gastos.services.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CustomUserDetailsService _registrationService;
    private AuthenticationManager authenticationManager;
    private JwtToken jwtToken;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Usuario user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
            return ResponseEntity.ok(jwtToken.generateToken(user.getUsername()));
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