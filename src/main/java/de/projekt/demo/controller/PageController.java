package de.projekt.demo.controller;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.entities.DataFile;
import de.projekt.demo.services.DataFileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping
public class PageController {

    private final DataFileService dataFileService;

    @Autowired
    public PageController(DataFileService dataFileService){
        this.dataFileService = dataFileService;
    }
    @GetMapping("/")
    public String zuLogin(){
        return "redirect:/auth/login";
    }

    @GetMapping("/dashboard")
    public String zuDashboard(HttpSession session, Model model){

        if(session.getAttribute("user") == null) return "redirect:auth/login";

        Benutzer benutzer = (Benutzer) session.getAttribute("user");

        List<DataFile> files = dataFileService.getAllByBenutzer(benutzer);

        model.addAttribute("files", files);

        return "dashboard";
    }

    @GetMapping("/dashboard/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/auth/login";
    }
}
