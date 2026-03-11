package de.projekt.demo.repositories;

import de.projekt.demo.entities.Benutzer;
import de.projekt.demo.entities.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository für die DataFile Entitöt
 */
public interface DataFileRepository extends JpaRepository<DataFile,Integer> {

    /**
     * Gibt für einen Benutzer alle Metadaten für die Dateien zurück, die den Besitzer
     * gehören
     * @param benutzer Der Benutzer
     * @return Alle Datafile Objekte, die ihn gehören
     */
    List<DataFile> findByBenutzer(Benutzer benutzer);
}
