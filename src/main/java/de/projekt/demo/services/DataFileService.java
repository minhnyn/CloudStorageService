package de.projekt.demo.services;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.entities.DataFile;
import de.projekt.demo.exceptions.InvalidDataException;
import de.projekt.demo.repositories.DataFileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Die Serviceklasse für die DataFile Entität
 */
@Service
@Transactional
public class DataFileService {

    private final DataFileRepository dataFileRepository;

    @Autowired
    public DataFileService(DataFileRepository dataFileRepository){
        this.dataFileRepository = dataFileRepository;
    }

    /**
     * Legt ein DataFile Objekt an
     * @param dataFile Das zu anlegende DataFile Objekt
     * @return Die id des DataFile Objektes
     */
    public int dataAnlegen(DataFile dataFile){
        return dataFileRepository.save(dataFile).getFileid();
    }

    /**
     * Gibt alle DataFile Objekte zurück
     * @return Liste aller DataFile Objekte
     */
    public List<DataFile> getAllDataFiles(){
        return dataFileRepository.findAll();
    }

    /**
     * Sucht ein DataFile anhand seiner Id
     * @param fileid Id des DataFile Objektes
     * @return Das entsprechende DataFile Objekt
     */
    public DataFile getDataFileById(int fileid){
        return dataFileRepository.findById(fileid).orElseThrow(InvalidDataException::new);
    }

    /**
     * Gibt alle DataFile Objekte zurück, die einen Besitzer gehören
     * @param benutzer Der Benutzer
     * @return Alle DataFile Objekte des Benutzers
     */
    public List<DataFile> getAllByBenutzer(Benutzer benutzer){
        return dataFileRepository.findByBenutzer(benutzer);
    }

    /**
     * Löscht ein DataFile Objekt anhand seiner Id
     * @param fileId die Id des DataFile Objektes
     */
    public void deleteDatafileById(int fileId){
        dataFileRepository.delete(dataFileRepository.findById(fileId).orElseThrow(InvalidDataException::new));
    }



}
