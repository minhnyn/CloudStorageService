package de.projekt.demo.repositories;

import de.projekt.demo.entities.Benutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenutzerRepository extends JpaRepository<Benutzer,String> {

    Optional<Benutzer> findBenutzerByEmail(String email);
}
