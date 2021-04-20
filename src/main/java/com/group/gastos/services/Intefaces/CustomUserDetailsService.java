package com.group.gastos.services.Intefaces;

import com.group.gastos.models.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;

public interface CustomUserDetailsService extends UserDetailsService {
    Usuario register(Usuario newUser) throws IOException, InterruptedException;
    boolean confirmToken(String token);
}
