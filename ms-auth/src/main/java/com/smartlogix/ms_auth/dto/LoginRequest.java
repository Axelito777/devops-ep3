package com.smartlogix.ms_auth.dto;

import lombok.Data;

/**
 * DTO con las credenciales enviadas por el cliente para iniciar sesión.
 *
 * @author SmartLogix Team
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
