package com.pharma.patientrecords.controllers;

import com.pharma.patientrecords.models.Patient;
import com.pharma.patientrecords.repositories.DossierRepository;
import com.pharma.patientrecords.repositories.PatientRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PatientController {
    final PatientRepository patientRepository;
    final DossierRepository dossierRepository;

    public PatientController(PatientRepository patientRepository, DossierRepository dossierRepository) {
        this.patientRepository = patientRepository;
        this.dossierRepository = dossierRepository;
    }

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    @ResponseBody
    List<Patient> getAll() {
        return patientRepository.findAll();
    }
}
