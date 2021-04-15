package com.group.gastos.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private String username;
    private String nickname;
    private String password;
    private Float sueldo;
}
