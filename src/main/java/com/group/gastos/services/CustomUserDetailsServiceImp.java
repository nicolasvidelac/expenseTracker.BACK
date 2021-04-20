package com.group.gastos.services;

import com.group.gastos.models.Usuario;
import com.group.gastos.others.registration.ConfirmationToken;
import com.group.gastos.others.email.EmailValidator;
import com.group.gastos.repositories.UsuarioRepository;
import com.group.gastos.services.Intefaces.ConfirmationTokenService;
import com.group.gastos.services.Intefaces.CustomUserDetailsService;
import com.group.gastos.services.Intefaces.ResumenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.invoke.WrongMethodTypeException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImp implements CustomUserDetailsService {

    private final UsuarioRepository _usuarioRepository;

    private final EmailValidator _emailValidator;
    private final PasswordEncoder _passwordEncoder;
    private final ConfirmationTokenService _confirmationTokenService;
    private final ResumenService _ResumenService;

    public Usuario register(Usuario newUser) throws IOException, InterruptedException {

        try {
            if (newUser.getUsername().isBlank() || newUser.getNickname().isBlank() || newUser.getPassword().isBlank()) {
                throw new NullPointerException("field cannot be empty");
            }
        } catch (Exception e) {
            throw new NullPointerException("field cannot be empty");
        }
        if (!_emailValidator.test(newUser.getUsername())) {
            throw new IllegalArgumentException("email invalid");
        }
        if (_usuarioRepository.findAll().stream().anyMatch(s -> s.getUsername().equals(newUser.getUsername()))) {
            throw new WrongMethodTypeException("email already in use");
        }


        newUser.setPassword(_passwordEncoder.encode(newUser.getPassword()));

        Usuario result = _usuarioRepository.save(newUser);

        //todo enviar email con token de confirmacion
//        ConfirmationToken confirmationToken = generateToken(result.getId());
//        sendEmail(confirmationToken.getToken(), result);

        _ResumenService.createResumen(result.getUsername());
        return result;
    }

    private ConfirmationToken generateToken(String id){
        ConfirmationToken confirmationToken = new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                id
        );

        return _confirmationTokenService.saveConfirmationToken(confirmationToken);
    }


    public Usuario login(Usuario user) throws Exception {
        Usuario result = _usuarioRepository.findAll().stream().filter(userN -> {
            return userN.getUsername().equals(user.getUsername())
                    && _passwordEncoder.matches(user.getPassword(), userN.getPassword())
                    ;
        })
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("user not found, check yourself"));

        try {
            if (result.getEnabled().equals(false)) {
                ConfirmationToken token = generateToken(user.getId());
                throw new Exception("user is not activated, please check your email");
            }
        } catch (NullPointerException e) {
            ConfirmationToken token = generateToken(user.getId());
            throw new Exception("user is not activated, please check your email");
        }

        return result;
    }

    public boolean confirmToken(String token) {
        ConfirmationToken confirmationToken = _confirmationTokenService.confirmToken(token);

        Usuario user = _usuarioRepository.findAll().stream().filter(s ->
                s.getId().equals(confirmationToken.getUser_id()))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("user for this token is not found"));

        user.setEnabled(true);

        _usuarioRepository.save(user);
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return _usuarioRepository.findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException("email: '" + username + "' not found"));

    }


}
