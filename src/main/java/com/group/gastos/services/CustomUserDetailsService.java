package com.group.gastos.services;

import com.group.gastos.models.EstadoResumen;
import com.group.gastos.models.Resumen;
import com.group.gastos.models.Usuario;
import com.group.gastos.others.email.EmailSender;
import com.group.gastos.others.registration.ConfirmationToken;
import com.group.gastos.others.registration.EmailValidator;
import com.group.gastos.repositories.ResumenRepository;
import com.group.gastos.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.WrongMethodTypeException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository _usuarioRepository;

    private final EmailValidator _emailValidator;
    private final PasswordEncoder _passwordEncoder;
    private final ConfirmationTokenService _confirmationTokenService;
    private final EmailSender _emailSender;

    private EstadoResumen _estadoResumenActivo;
    private final ResumenRepository _resumenRepository;

    public Usuario register(Usuario newUser) {

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

        ConfirmationToken confirmationToken = generateToken(result.getId());

//        sendEmail(confirmationToken.getToken(), result);

        _resumenRepository.save(new Resumen(result.getSueldo(), 150F, result, _estadoResumenActivo));

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

    private void sendEmail(String token, Usuario result){
        String link = "http://localhost:8080/api/v1/auth/register/confirmation?token=" + token;
        _emailSender.send(result.getUsername(), buildEmail(result.getNickname(), link) );
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
                sendEmail(token.getToken(), user);
                throw new Exception("user is not activated, please check your email");
            }
        } catch (NullPointerException e) {
            ConfirmationToken token = generateToken(user.getId());
            sendEmail(token.getToken(), user);
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


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
