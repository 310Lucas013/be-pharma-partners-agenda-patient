package com.pharma.patientrecords.repositories;

import com.pharma.patientrecords.models.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DossierRepository  extends JpaRepository<Dossier, Long> {
}
