package de.projekt.demo.controller;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.exceptions.InvalidEmailException;
import de.projekt.demo.exceptions.InvalidPasswordException;
import de.projekt.demo.services.BenutzerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller/Backend endpunkt für die Authenfikation zur Website, also das Einloggen und Registrieren
 */
@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final BenutzerService benutzerService;

    @Autowired
    public AuthenticationController(BenutzerService benutzerService){
        this.benutzerService = benutzerService;
    }


    /**
     * Weiterleitung zur Loginseite
     * @return Die HTML Datei der Loginseite
     */
    @GetMapping("/login")
    public String zuLogin(){
        return "loginscreen";
    }

    /**
     * Weiterleitung zur Registrierungsseite
     * @return Die HTML Datei der Registrierungsseite
     */
    @GetMapping("/register")
    public String zuRegister(){
        return "signupscreen";
    }

    /**
     * Anmeldung eines Nutzers zur Seite
     * @param email Eingegebene Email
     * @param password Eingegebenes Passwort
     * @param session Die Session des Nutzers
     * @param model Das Model des Nutzers
     * @return Weiterleitung des Nutzers zur entsprechenden Seite als String
     */
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model){
        try {
            Benutzer benutzer = benutzerService.benutzerEinloggen(email, password);
            session.setAttribute("user",benutzer);
            return "redirect:/dashboard";
        } catch (Exception e){
            model.addAttribute("loginError","Email oder Passwort stimmen nicht");
            return "loginscreen";
        }
    }

    /**
     * Registrierung eines Nutzers zur Website
     * @param email Eingegebene Email
     * @param password Eingegebenes Passwort
     * @param key Eingegebener Key
     * @param model Das Model des Nutzers
     * @return Die Weiterleitung zur eintsprechenden Seite als String
     */
    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, @RequestParam String key, Model model){
        try {
            benutzerService.benutzerAnlegen(email, password, key);
            return "redirect:/auth/login";
        } catch (InvalidEmailException e){
            model.addAttribute("emailError", e.getMessage());
        } catch (InvalidPasswordException e){
            model.addAttribute("passwordError", e.getMessage());
        } catch (IllegalArgumentException e){
            model.addAttribute("keyError", e.getMessage());

        }
        return "signupscreen";
    }
}
