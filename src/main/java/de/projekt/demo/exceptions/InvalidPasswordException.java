package de.projekt.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception für die ungültige eingabe eines Passworts
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Passwort muss mindestens 8 Zeichen lang sein");
    }
}
