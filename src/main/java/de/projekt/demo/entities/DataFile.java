package de.projekt.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Entity
@Getter
@Setter
public class DataFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileid;

    // Originaler Name
    private String originalFileName;

    // Gespeicherter Name im Dateisystem, dient zur suche
    private String storedFileName;

    private String contentType;

    private Long size;

    //Dargestellte Größe
    private String sizeForVisuell;

    private LocalDate uploadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benutzer_email", nullable = false)
    private Benutzer benutzer;


    public DataFile(String originalFileName,
                      String storedFileName,
                      String contentType,
                      Long size,
                      Benutzer benutzer, String sizeForVisuell) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.contentType = contentType;
        this.size = size;
        this.uploadTime = LocalDate.now();
        this.benutzer = benutzer;
        this.sizeForVisuell = sizeForVisuell;
    }



    public DataFile() {

    }
}
