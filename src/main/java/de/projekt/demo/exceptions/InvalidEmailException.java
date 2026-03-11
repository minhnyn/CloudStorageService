package de.projekt.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception für, wenn man eine ungültige E-Mail eingibt
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Email existiert bereits oder ist ungültig");
    }
}
