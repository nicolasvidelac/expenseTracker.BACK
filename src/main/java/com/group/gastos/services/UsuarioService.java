package com.group.gastos.services;

import com.group.gastos.models.Usuario;
import com.group.gastos.others.email.EmailValidator;
import com.group.gastos.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository _usuarioRepository;
    private final PasswordEncoder _passwordEncoder;
    private final EmailValidator _emailValidator;

    public Usuario updateUser(String username, Usuario updateUser) {

        //busco el usuario, para ver si existe, si existe, me traigo el id
        Usuario oldUser =  _usuarioRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("username not found"));

        //cambio el email, si es que trajo
        if (updateUser.getUsername() != null){
            if (_emailValidator.test(updateUser.getUsername())) {
                oldUser.setUsername(updateUser.getUsername());
            } else {
                throw new IllegalArgumentException("email invalid");
            }
        }

        if(updateUser.getNickname() != null){
            oldUser.setNickname(updateUser.getNickname());
        }

        if(updateUser.getPassword() != null){
            oldUser.setPassword(_passwordEncoder.encode(updateUser.getPassword()));
        }

        if(updateUser.getSueldo() != null){
            oldUser.setSueldo(updateUser.getSueldo());
        }

        return _usuarioRepository.save(oldUser);

    }

    public Usuario findUsuario(String username) {
        Usuario result = _usuarioRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("user not found"));
        return result;
    }


    public void deleteUsuario(String username) {
        Usuario user = _usuarioRepository.findAll().stream().filter(s -> s.getUsername().equals(username))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        user.setEnabled(false);
        _usuarioRepository.save(user);
    }
}
