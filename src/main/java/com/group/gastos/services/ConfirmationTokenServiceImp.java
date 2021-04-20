package com.group.gastos.services;

import com.group.gastos.others.registration.ConfirmationToken;
import com.group.gastos.repositories.ConfirmationTokenRepository;
import com.group.gastos.services.Intefaces.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImp implements ConfirmationTokenService {

    private final ConfirmationTokenRepository _confirmationTokenRepository;

    public ConfirmationToken saveConfirmationToken(ConfirmationToken confirmationToken) {
        return _confirmationTokenRepository.save(confirmationToken);
    }

    @Transactional
    public ConfirmationToken confirmToken(String token) {
        ConfirmationToken confirmationToken = _confirmationTokenRepository.findByToken(token).orElseThrow(
                () -> new RuntimeException("token not found")
        );
        if (confirmationToken.getConfirmedAt().isAfter(
                LocalDateTime.of(0,1,1,0,1,0,0)
        )) {
            throw new RuntimeException("the email is already confirmed");
        }

        if (confirmationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("the token already expired");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());

        return _confirmationTokenRepository.save(confirmationToken);
    }
}
