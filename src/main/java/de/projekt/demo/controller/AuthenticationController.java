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


@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final BenutzerService benutzerService;

    @Autowired
    public AuthenticationController(BenutzerService benutzerService){
        this.benutzerService = benutzerService;
    }


    @GetMapping("/login")
    public String zuLogin(){
        return "loginscreen";
    }

    @GetMapping("/register")
    public String zuRegister(){
        return "signupscreen";
    }

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
