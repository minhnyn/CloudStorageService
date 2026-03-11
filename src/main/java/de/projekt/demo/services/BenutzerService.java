package de.projekt.demo.services;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.entities.RegisterRequest;
import de.projekt.demo.exceptions.InvalidEmailException;
import de.projekt.demo.exceptions.InvalidPasswordException;
import de.projekt.demo.repositories.BenutzerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviceklasse für den Benutzer
 */
@Transactional
@Service
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;

    @Autowired
    public BenutzerService(BenutzerRepository benutzerRepository){
        this.benutzerRepository = benutzerRepository;
    }

    /**
     * Legt einen Benutzer an
     * @param registerRequest Das Registerrequest Objekt, welches aus den eingegebenen
     *                        Email, Passwort und Key zusammensetzt
     * @return Die Email des neu zugelegten Besitzer
     */
    public String benutzerAnlegen(RegisterRequest registerRequest){
        checkRequest(registerRequest);
        Benutzer benutzer = new Benutzer(registerRequest.getEmail(),registerRequest.getPassword());
        benutzerRepository.save(benutzer);
        return benutzer.getEmail();
    }

    /**
     * Legt einen neuen Benutzer an
     * @param email Die eingegebene Email
     * @param password Das eingegebene Passwort
     * @param key Der eingegebene Key
     * @return Die Email des registrierten Benutzer
     * @throws IllegalArgumentException Üngultige Eingabe
     */
    public String benutzerAnlegen(String email, String password, String key) throws IllegalArgumentException{
        checkRequest(email,password,key);
        benutzerRepository.save(new Benutzer(email,password));
        return email;
    }

    /**
     * Meldet einen Benutzer an
     * @param email Die eingegebene Email
     * @param password Das eingegebene Passwort
     * @return Das Benutzerobjekt des Logins
     * @throws IllegalArgumentException Ungültige Eingabe
     */
    public Benutzer benutzerEinloggen(String email, String password) throws IllegalArgumentException{
        Benutzer benutzer = getBenutzerByEmail(email);
        if(!password.equals(benutzer.getPassword())) throw new InvalidPasswordException();
        return benutzer;
    }

    /**
     * Sucht einen Benutzer anhand seiner Email
     * @param email Eingegebene Email
     * @return Den entsprechenden Benutzer
     * @throws InvalidEmailException Ungültige Email
     */
    public Benutzer getBenutzerByEmail(String email) throws InvalidEmailException{
        return benutzerRepository.findBenutzerByEmail(email).orElseThrow(InvalidEmailException::new);
    }

    /**
     * Gibt alle Benutzer zurück
     * @return Alle Benutzer
     */
    public List<Benutzer> getAllBenutzer(){
        return benutzerRepository.findAll();
    }

    /**
     * Benutzer löschen anhand seiner Email
     * @param email Die eingegebene Email
     */
    public void deleteBenutzerByEmail(String email){
        Benutzer benutzer = getBenutzerByEmail(email);
        benutzerRepository.delete(benutzer);
    }

    /**
     * Aktualisiert das Passwort
     * @param email Die Email des Benutzers
     * @param passwort Das neue Passwort
     * @return Die Email des Benutzers
     */
    public String aktualisierePasswort(String email, String passwort){
        Benutzer benutzer = getBenutzerByEmail(email);
        benutzer.setPassword(passwort);
        return email;
    }

    /**
     * Überprüft die gültigkeit der eingabe Daten und wirft entsprechende Exceptions
     * @param registerRequest Das DTO
     */
    private void checkRequest(RegisterRequest registerRequest){
        checkRequest(registerRequest.getEmail(),registerRequest.getPassword(),registerRequest.getKey());
    }

    /**
     * Überprüft die gültigkeit der eingabe Daten und wirft entsprechende Exceptions
     * @param email Die eingegebene Email
     * @param password Das eingegebene Passwort
     * @param key Der eingegebene Key
     */
    private void checkRequest(String email, String password, String key){
        if (!email.matches(".+?@.+?") || benutzerRepository.findBenutzerByEmail(email).isPresent()) throw new InvalidEmailException();
        if (password.length() < 8) throw new InvalidPasswordException();
        if (!key.equals("test")) throw new IllegalArgumentException("Falscher Key");
    }

}
