package de.projekt.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Die Benutzer Entität
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Benutzer {

    @Id
    private String email;

    private String password;

    public Benutzer(String email, String password) {
        this.email = email;
        this.password = password;
    }

}

