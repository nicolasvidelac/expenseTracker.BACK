package com.group.gastos.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthDTO {
    private String username;
    private String password;
    private String nickname;
}
