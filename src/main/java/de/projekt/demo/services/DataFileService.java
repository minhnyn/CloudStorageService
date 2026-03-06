package de.projekt.demo.services;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.entities.DataFile;
import de.projekt.demo.exceptions.InvalidDataException;
import de.projekt.demo.repositories.DataFileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DataFileService {

    private DataFileRepository dataFileRepository;

    @Autowired
    public DataFileService(DataFileRepository dataFileRepository){
        this.dataFileRepository = dataFileRepository;
    }

    public int dataAnlegen(DataFile dataFile){
        return dataFileRepository.save(dataFile).getFileid();
    }

    public List<DataFile> getAllDataFiles(){
        return dataFileRepository.findAll();
    }
    public DataFile getDataFileById(int fileid){
        return dataFileRepository.findById(fileid).orElseThrow(InvalidDataException::new);
    }

    public List<DataFile> getAllByBenutzer(Benutzer benutzer){
        return dataFileRepository.findByBenutzer(benutzer);
    }

    public void deleteDatafileById(int fileId){
        dataFileRepository.delete(dataFileRepository.findById(fileId).orElseThrow(InvalidDataException::new));
    }



}
