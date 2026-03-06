package de.projekt.demo.controller;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.services.BenutzerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private BenutzerService benutzerService;

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
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session){

        try {
            Benutzer benutzer = benutzerService.benutzerEinloggen(email, password);
            session.setAttribute("user",benutzer);
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e){
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, @RequestParam String key){
        try {
            benutzerService.benutzerAnlegen(email, password, key);
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e){
            return "redirect:/auth/register";
        }

    }
}
