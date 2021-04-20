package com.group.gastos.servicesDTO.Interfaces;

import com.group.gastos.models.dtos.AuthDTO;
import com.group.gastos.models.dtos.UsuarioDTO;

import java.io.IOException;

public interface UsuarioServiceDTO {
    UsuarioDTO login(AuthDTO authDTO);
    UsuarioDTO register(AuthDTO authDTO) throws IOException, InterruptedException;
    UsuarioDTO updateUser(String username, UsuarioDTO usuarioDTO);
    UsuarioDTO getUsuario(String username);
    void deleteUsuario(String username);
    boolean confirmToken(String token);
}
