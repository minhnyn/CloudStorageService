package de.projekt.demo.exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException() {
        super("Ungültige Eingabe");
    }
}
