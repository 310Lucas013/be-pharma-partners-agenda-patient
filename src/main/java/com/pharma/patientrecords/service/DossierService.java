package com.pharma.patientrecords.service;

import com.pharma.patientrecords.models.Dossier;
import com.pharma.patientrecords.repositories.DossierRepository;
import org.springframework.stereotype.Service;

@Service
public class DossierService {
    private final DossierRepository dossierRepository;

    public DossierService(DossierRepository dossierRepository) {
        this.dossierRepository = dossierRepository;
    }

    public Dossier save(Dossier dossier) {
        return dossierRepository.save(dossier);
    }
}
