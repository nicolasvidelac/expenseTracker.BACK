package com.group.gastos.controllers;


import com.group.gastos.services.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService _usuarioService;


}