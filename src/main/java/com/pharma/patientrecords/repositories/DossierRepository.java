package com.pharma.patientrecords.repositories;

import com.pharma.patientrecords.models.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossierRepository  extends JpaRepository<Dossier, Long> {
}
