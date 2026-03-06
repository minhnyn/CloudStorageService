package de.projekt.demo.services;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.entities.DataFile;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UploadService {

    private DataFileService dataFileService;

    private BenutzerService benutzerService;

    private final String uploadDir = "uploads/";

    @Autowired
    public UploadService(DataFileService dataFileService, BenutzerService benutzerService){
        this.dataFileService = dataFileService;
        this.benutzerService = benutzerService;
    }

    public DataFile uploadData(String email, MultipartFile file) throws IOException {
        Benutzer benutzer = benutzerService.getBenutzerByEmail(email);

        Files.createDirectories(Paths.get(uploadDir));

        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, uniqueFileName);

        Files.write(filePath, file.getBytes());

        DataFile fileEntity = new DataFile();
        fileEntity.setOriginalFileName(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setStoredFileName(uniqueFileName);
        fileEntity.setBenutzer(benutzer);
        fileEntity.setUploadTime(LocalDate.now());
        fileEntity.setSize(file.getSize());
        fileEntity.setSizeForVisuell(readableSize(file.getSize()));

        dataFileService.dataAnlegen(fileEntity);

        return fileEntity;
    }

    public ResponseEntity<Resource> downloadData(Benutzer benutzer, int fileId) throws MalformedURLException {

        DataFile dataFile = dataFileService.getDataFileById(fileId);

        //Sicherheitscheck
        if (!dataFile.getBenutzer().getEmail().equals(benutzer.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Path path = Paths.get(uploadDir, dataFile.getStoredFileName());

        Resource resource = new UrlResource(path.toUri());

        // 5️⃣ Datei zurückgeben
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dataFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + dataFile.getOriginalFileName() + "\"")
                .body(resource);
    }

    public ResponseEntity<String> removeData(Benutzer benutzer, int fileId){

        DataFile dataFile = dataFileService.getDataFileById(fileId);

        if (!dataFile.getBenutzer().getEmail().equals(benutzer.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        dataFile.setBenutzer(null);
        dataFileService.deleteDatafileById(fileId);

        Path path = Paths.get(uploadDir, dataFile.getStoredFileName());

        try{
            if(Files.deleteIfExists(path)){
                System.out.println("File deleted");
            } else {
                System.out.println("File does not exist");
            }
        } catch (IOException e){
            System.out.println("Delete failed");
            e.printStackTrace();
        }

        return ResponseEntity.ok("Hat geklappt");
    }

    public static String readableSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String units = "KMGTPE";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), units.charAt(exp-1));
    }


}
