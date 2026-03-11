package de.projekt.demo.controller;

import de.projekt.demo.entities.Benutzer;

import de.projekt.demo.entities.DataFile;
import de.projekt.demo.services.UploadService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller/Backend endpunkt für die Funktionen der Dashboardseite zur Website
 */
@RestController
@CrossOrigin
@RequestMapping("/dashboard")
public class DashbordController {

    private final UploadService uploadService;

    @Autowired
    public DashbordController(UploadService uploadService){
        this.uploadService = uploadService;
    }

    /**
     * Hochladen einer Datei
     * @param file Die zu hochladende Datei
     * @param session Die Session des Nutzers
     * @return Json des DataFile Objektes
     * @throws IOException
     */
    @PostMapping("/upload")
    public DataFile uploadFile(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        Object benutzerTemp = session.getAttribute("user");
        Benutzer benutzer = (Benutzer) benutzerTemp;

        return uploadService.uploadData(benutzer.getEmail(),file);
    }

    /**
     * Benennt eine Datei um
     * @param fileId Die Id des DataFile Objektes
     * @param newName Der neue Name
     * @param session Die Session des Benutzers
     * @return Json des DataFile Objektes
     * @throws IOException
     */
    @PostMapping("/rename/{fileId}")
    public DataFile renameFile(@PathVariable int fileId, @RequestParam String newName, HttpSession session) throws IOException {

        Benutzer benutzer = (Benutzer) session.getAttribute("user");

        return uploadService.changeFileName(benutzer,fileId,newName);
    }

    /**
     * Downloaded eine Datei
     * @param fileId Die Id der entsprechenden DataFile Objekt
     * @param session Die Session des Nutzers
     * @return Die Response, ob dies Erfolgreich war und wenn ja dann auch die Datei
     * @throws IOException
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable int fileId, HttpSession session) throws IOException {

        Benutzer benutzer = (Benutzer) session.getAttribute("user");

        return uploadService.downloadData(benutzer,fileId);

    }

    /**
     * Löscht eine Datei
     * @param fileId Die Id des DataFile Objektes der zu löschenden Datei
     * @param session Die Session des Nutzers
     * @return Eine Meldung, ob die aktion erfolgreich war
     */
    @DeleteMapping("/delete/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@PathVariable int fileId, HttpSession session){

        Benutzer benutzer = (Benutzer) session.getAttribute("user");

        return uploadService.removeData(benutzer,fileId);
    }

}
