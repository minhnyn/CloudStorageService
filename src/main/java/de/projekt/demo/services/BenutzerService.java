package de.projekt.demo.services;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.entities.DataFile;
import de.projekt.demo.entities.RegisterRequest;
import de.projekt.demo.exceptions.InvalidDataException;
import de.projekt.demo.repositories.BenutzerRepository;
import de.projekt.demo.repositories.DataFileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Transactional
@Service
public class BenutzerService {

    private BenutzerRepository benutzerRepository;


    @Autowired
    public BenutzerService(BenutzerRepository benutzerRepository){
        this.benutzerRepository = benutzerRepository;
    }

    public String benutzerAnlegen(RegisterRequest registerRequest){
        if(!checkRequest(registerRequest)) throw new InvalidDataException();
        Benutzer benutzer = new Benutzer(registerRequest.getEmail(),registerRequest.getPassword());
        benutzerRepository.save(benutzer);
        return benutzer.getEmail();
    }

    public String benutzerAnlegen(String email, String password, String key) throws IllegalArgumentException{
        if (!checkRequest(email,password,key) || benutzerRepository.findBenutzerByEmail(email).isPresent()) throw new IllegalArgumentException();
        benutzerRepository.save(new Benutzer(email,password));
        return email;
    }

    public Benutzer benutzerEinloggen(String email, String password) throws IllegalArgumentException{
        Benutzer benutzer = getBenutzerByEmail(email);
        if(!password.equals(benutzer.getPassword())) throw new IllegalArgumentException();
        return benutzer;
    }

    public Benutzer getBenutzerByEmail(String email) throws IllegalArgumentException{
        return benutzerRepository.findBenutzerByEmail(email).orElseThrow(IllegalArgumentException::new);
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

    private boolean checkRequest(RegisterRequest registerRequest){
        return (registerRequest.getEmail().matches(".+?@.+?") && registerRequest.getPassword().length() >= 8 && registerRequest.getKey().equals("test"));
    }

    private boolean checkRequest(String email, String password, String key){
        return (email.matches(".+?@.+?") && password.length() >= 8 && key.equals("test"));

    }

}
