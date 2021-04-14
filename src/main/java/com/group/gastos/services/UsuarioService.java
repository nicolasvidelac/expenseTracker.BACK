package com.group.gastos.services;

import com.group.gastos.models.Usuario;
import com.group.gastos.others.registration.EmailValidator;
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

    public Usuario updateUser(Usuario updateUser) {
        try {
            if (updateUser.getUsername().isBlank() || updateUser.getNickname().isBlank() ||
                    updateUser.getPassword().isBlank()) {
                throw new NullPointerException("field cannot be empty");
            }
        } catch (Exception e) {
            throw new NullPointerException("field cannot be empty");
        }
        if (!_emailValidator.test(updateUser.getUsername())) {
            throw new IllegalArgumentException("email invalid");
        }
        Usuario oldUser = _usuarioRepository.findById(updateUser.getId()).orElseThrow();
        updateUser.setPassword(_passwordEncoder.encode(updateUser.getPassword()));

        return _usuarioRepository.insert(updateUser);

    }

    public Usuario findUsuario(String email) {
        Usuario result = _usuarioRepository.findAll().stream().filter(user -> user.getUsername().equals(email))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("email not found"));
        return result;
    }


    public void deleteUsuario(String id) {
        Usuario user = _usuarioRepository.findAll().stream().filter(s -> s.getId().equals(id))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("user not found"));

        _usuarioRepository.delete(user);
    }


}
