package com.group.gastos.services.Intefaces;

import com.group.gastos.models.Usuario;

public interface UsuarioService {
    Usuario updateUser(String username, Usuario updateUser);
    Usuario findUsuario(String username);
    void deleteUsuario(String username);
}
