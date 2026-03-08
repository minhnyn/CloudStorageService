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

@Transactional
@Service
public class BenutzerService {

    private final BenutzerRepository benutzerRepository;


    @Autowired
    public BenutzerService(BenutzerRepository benutzerRepository){
        this.benutzerRepository = benutzerRepository;
    }

    public String benutzerAnlegen(RegisterRequest registerRequest){
        checkRequest(registerRequest);
        Benutzer benutzer = new Benutzer(registerRequest.getEmail(),registerRequest.getPassword());
        benutzerRepository.save(benutzer);
        return benutzer.getEmail();
    }

    public String benutzerAnlegen(String email, String password, String key) throws IllegalArgumentException{
        checkRequest(email,password,key);
        benutzerRepository.save(new Benutzer(email,password));
        return email;
    }

    public Benutzer benutzerEinloggen(String email, String password) throws IllegalArgumentException{
        Benutzer benutzer = getBenutzerByEmail(email);
        if(!password.equals(benutzer.getPassword())) throw new InvalidPasswordException();
        return benutzer;
    }

    public Benutzer getBenutzerByEmail(String email) throws IllegalArgumentException{
        return benutzerRepository.findBenutzerByEmail(email).orElseThrow(InvalidEmailException::new);
    }

    public List<Benutzer> getAllBenutzer(){
        return benutzerRepository.findAll();
    }

    public void deleteBenutzerByEmail(String email){
        Benutzer benutzer = getBenutzerByEmail(email);
        benutzerRepository.delete(benutzer);
    }

    public String aktualisierePasswort(String email, String passwort){
        Benutzer benutzer = getBenutzerByEmail(email);
        benutzer.setPassword(passwort);
        return email;
    }

    private void checkRequest(RegisterRequest registerRequest){
        checkRequest(registerRequest.getEmail(),registerRequest.getPassword(),registerRequest.getKey());
    }

    private void checkRequest(String email, String password, String key){
        if (!email.matches(".+?@.+?") || benutzerRepository.findBenutzerByEmail(email).isPresent()) throw new InvalidEmailException();
        if (password.length() < 8) throw new InvalidPasswordException();
        if (!key.equals("test")) throw new IllegalArgumentException("Falscher Key");
    }

}
