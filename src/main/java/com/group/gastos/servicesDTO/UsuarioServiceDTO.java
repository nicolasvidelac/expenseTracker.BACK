package com.group.gastos.servicesDTO;

import com.group.gastos.models.Usuario;
import com.group.gastos.models.dtos.AuthDTO;
import com.group.gastos.models.dtos.UsuarioDTO;
import com.group.gastos.others.jwt.JwtToken;
import com.group.gastos.services.CustomUserDetailsService;
import com.group.gastos.services.UsuarioService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class UsuarioServiceDTO {

    private final UsuarioService _usuarioService;
    private final ModelMapper _modelMapper;
    private final CustomUserDetailsService _registrationService;
    private final JwtToken jwtToken;
    private final AuthenticationManager authenticationManager;

    public UsuarioDTO login(AuthDTO authDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.getUsername(),
                        authDTO.getPassword()
                )
        );
        Usuario usuario = _usuarioService.findUsuario(authDTO.getUsername());
        UsuarioDTO usuarioDTO = _modelMapper.map(usuario, UsuarioDTO.class);
        usuarioDTO.setToken(jwtToken.generateToken(usuarioDTO.getUsername()));

        return usuarioDTO;
    }

    public UsuarioDTO register(AuthDTO authDTO) throws IOException, InterruptedException {
        Usuario usuario = _modelMapper.map(authDTO, Usuario.class);
        usuario = _registrationService.register(usuario);

        return _modelMapper.map(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO updateUser(String username, UsuarioDTO usuarioDTO) {
        Usuario usuario = _modelMapper.map(usuarioDTO, Usuario.class);
        usuario = _usuarioService.updateUser(username, usuario);
        return _modelMapper.map(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO getUsuario(String username) {
        return _modelMapper.map(_usuarioService.findUsuario(username), UsuarioDTO.class);
    }

    public void deleteUsuario(String username) {
        _usuarioService.deleteUsuario(username);
    }

    public boolean confirmToken(String token) {
        return _registrationService.confirmToken(token);
    }


}
