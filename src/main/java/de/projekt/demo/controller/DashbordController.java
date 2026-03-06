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


@RestController
@CrossOrigin
@RequestMapping("/dashboard")
public class DashbordController {

    private UploadService uploadService;

    @Autowired
    public DashbordController(UploadService uploadService){
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public DataFile uploadFile(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        Object benutzerTemp = session.getAttribute("user");
        Benutzer benutzer = (Benutzer) benutzerTemp;

        return uploadService.uploadData(benutzer.getEmail(),file);
    }

    @PostMapping("/rename/{fileId}")
    public DataFile renameFile(@PathVariable int fileId, @RequestParam String newName, HttpSession session) throws IOException {
        Benutzer benutzer = (Benutzer) session.getAttribute("user");
        return uploadService.changeFileName(benutzer,fileId,newName);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable int fileId, HttpSession session) throws IOException {

        Benutzer benutzer = (Benutzer) session.getAttribute("user");

        return uploadService.downloadData(benutzer,fileId);

    }

    @DeleteMapping("/delete/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@PathVariable int fileId, HttpSession session){

        Benutzer benutzer = (Benutzer) session.getAttribute("user");

        return uploadService.removeData(benutzer,fileId);
    }

}
