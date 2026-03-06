package de.projekt.demo.repositories;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.entities.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataFileRepository extends JpaRepository<DataFile,Integer> {

    List<DataFile> findByBenutzer(Benutzer benutzer);
}
