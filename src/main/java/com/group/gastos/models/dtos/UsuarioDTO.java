package com.group.gastos.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private String username;
    private String nickname;
    private Float sueldo;
    private String token;
}
