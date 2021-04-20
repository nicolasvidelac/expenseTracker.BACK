package com.group.gastos.services.Intefaces;

import com.group.gastos.others.registration.ConfirmationToken;
import org.springframework.stereotype.Service;

public interface ConfirmationTokenService {
    ConfirmationToken saveConfirmationToken(ConfirmationToken confirmationToken);
    ConfirmationToken confirmToken(String token);
}
