package de.projekt.demo.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO für das Registrieren. Ist keine Entität
 */
@Getter
@Setter
public class RegisterRequest {

    private String email;
    private String password;
    private String key;


}
