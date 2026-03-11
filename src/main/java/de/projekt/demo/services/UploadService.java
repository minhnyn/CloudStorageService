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
import java.util.UUID;

/**
 * Serviceklasse, die für das Hochladen, Löschen, Umbenennen von Dateien zuständig ist
 */
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

    /**
     * Lädt eine Datei hoch, mit einer Größenbegrenzung von fast 100Mb
     * @param email Die Email des Benutzers, der die Datei hochlädt
     * @param file Die Datei
     * @return Das DataFile Objekt zur hochgeladenen Datei
     * @throws IOException Fehler beim Input oder Output der Datei
     */
    public DataFile uploadData(String email, MultipartFile file) throws IOException {
        if(file.getSize() > 100000000){
            throw new IllegalArgumentException();
        }
        Benutzer benutzer = benutzerService.getBenutzerByEmail(email);

        Files.createDirectories(Paths.get(uploadDir));

        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, uniqueFileName);

        //Schreibt die Datei im Dateisystem
        Files.write(filePath, file.getBytes());

        DataFile dataFile = createDataFileByMultiPartFile(file,uniqueFileName,benutzer);

        dataFileService.dataAnlegen(dataFile);

        return dataFile;
    }

    /**
     * Der Benutzer lädt eine Datei herunter
     * @param benutzer Der Benutzer der die Datei herunterlädt
     * @param fileId Die Id des DateiFile Objekt der zu herunterladenen Datei
     * @return Eine Response, ob das Herunterladen erfolgreich war, mit der Datei
     * @throws MalformedURLException
     */
    public ResponseEntity<Resource> downloadData(Benutzer benutzer, int fileId) throws MalformedURLException {

        DataFile dataFile = dataFileService.getDataFileById(fileId);

        //Sicherheitscheck
        if (!dataFile.getBenutzer().getEmail().equals(benutzer.getEmail())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Resource resource = getResource(dataFile);

        //Datei zurückgeben
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dataFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + dataFile.getOriginalFileName() + "\"")
                .body(resource);
    }

    /**
     * Löscht/Entfernt eine Datei aus dem Dateisystem
     * @param benutzer Der Benutzer dem diese Datei gehört
     * @param fileId Die Id des DataFile der zu entfernenden Datei
     * @return Eine Rückmeldung, ob das Löschen erfolgreich war
     */
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

    /**
     * Wandelt eine Anzahl an Bytes in eine lesbarere Größe um, wobei die Darstellung
     * String ist
     * @param bytes Die Anzahl an Bytes
     * @return Die lesbare Darstellung
     */
    public static String readableSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String units = "KMGTPE";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), units.charAt(exp-1));
    }

    /**
     * Ändert den Namen einer Datei
     * @param benutzer Der Benutzer der Datei
     * @param dataFileId Die Id des entsprechenden DataFile Objektes
     * @param newName Der neue Name
     * @return Die abgeänderte DataFile
     * @throws IOException Fehler beim Input oder Output der Datei
     * @throws IllegalArgumentException Ungültiger Name
     */
    public DataFile changeFileName(Benutzer benutzer, int dataFileId, String newName) throws IOException, IllegalArgumentException {

        if (newName.contains(".") || newName.contains("/") || newName.contains("\\") || newName.isBlank()) {
            throw new IllegalArgumentException("Ungültiger Dateiname");
        }

        DataFile dataFile = dataFileService.getDataFileById(dataFileId);

        //Sicherheitscheck
        if (!dataFile.getBenutzer().getEmail().equals(benutzer.getEmail())) throw new IllegalAccessError("Unerlaubter Zugriff");

        Path oldFilePath = Paths.get(uploadDir, dataFile.getStoredFileName());

        //Bildung des neuen Namens
        int suffixPos = dataFile.getOriginalFileName().lastIndexOf(".");
        String suffix = suffixPos >= 0 ? dataFile.getOriginalFileName().substring(suffixPos) : "";
        String newOriginalName = newName+suffix;

        //Bildung des neuen Pfades
        String newUniqueFileName = UUID.randomUUID() + "_" + newOriginalName;
        Path newFilePath = Paths.get(uploadDir, newUniqueFileName);

        //Namensänderung
        Files.move(oldFilePath,newFilePath);

        //Änderung des DataFile Objektes
        dataFile.setStoredFileName(newUniqueFileName);
        dataFile.setOriginalFileName(newOriginalName);

        return dataFile;
    }

    /**
     * Gibt die Resource/Datei eines DataFile Objektes zurück
     * @param dataFile Die DataFile
     * @return Die Datei die von dem DataFile Objekt angegeben wurde
     * @throws MalformedURLException
     */
    private Resource getResource(DataFile dataFile) throws MalformedURLException {

        Path path = Paths.get(uploadDir, dataFile.getStoredFileName());

        return new UrlResource(path.toUri());
    }

    /**
     * Erzeugt anhand einer MultipartFile, einzigartigen Namen und Benutzer
     * das entsprechende DataFile Objekt
     * @param file Die MultipartFile
     * @param uniqueFileName Der Name, womit die Datei im Dateisystem gesucht wird
     * @param benutzer Der Benutzer, dem die Datei gehört
     * @return Das entsprechende DataFile Objekt
     */
    private DataFile createDataFileByMultiPartFile(MultipartFile file, String uniqueFileName, Benutzer benutzer){
        DataFile fileEntity = new DataFile();
        fileEntity.setOriginalFileName(file.getOriginalFilename());
        fileEntity.setContentType(file.getContentType());
        fileEntity.setStoredFileName(uniqueFileName);
        fileEntity.setBenutzer(benutzer);
        fileEntity.setUploadDate(LocalDate.now());
        fileEntity.setSize(file.getSize());
        fileEntity.setSizeForVisuell(readableSize(file.getSize()));

        return fileEntity;
    }




}
