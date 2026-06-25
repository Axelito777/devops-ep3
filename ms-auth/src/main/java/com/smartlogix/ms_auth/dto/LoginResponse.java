package com.smartlogix.ms_auth.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * DTO con el resultado de una autenticación exitosa: el token JWT generado
 * junto con el rol y el email del usuario autenticado.
 *
 * @author SmartLogix Team
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String rol;
    private String email;
}
