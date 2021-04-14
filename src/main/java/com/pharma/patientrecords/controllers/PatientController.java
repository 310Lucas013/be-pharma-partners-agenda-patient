package com.pharma.patientrecords.controllers;

import com.pharma.patientrecords.models.Dossier;
import com.pharma.patientrecords.models.Patient;
import com.pharma.patientrecords.models.dto.PatientDto;
import com.pharma.patientrecords.models.enums.Gender;
import com.pharma.patientrecords.repositories.DossierRepository;
import com.pharma.patientrecords.repositories.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/patients")
public class PatientController {
    final PatientRepository patientRepository;
    final DossierRepository dossierRepository;

    public PatientController(PatientRepository patientRepository, DossierRepository dossierRepository) {
        this.patientRepository = patientRepository;
        this.dossierRepository = dossierRepository;
    }

    @PostMapping()
    public ResponseEntity<?> savePatient(@RequestBody PatientDto patientDto) {
        System.out.println(patientDto);
        Patient p = new Patient();
        p.setFirstName(patientDto.getFirstName());
        p.setLastName(patientDto.getLastName());
        p.setMiddleName(patientDto.getMiddleName());
        p.setGender(patientDto.getGender());
        p.setDateOfBirth(patientDto.getDateOfBirth());
        p.setPhoneNumber(patientDto.getPhoneNumber());
        Dossier dossier = new Dossier(patientDto.getDossierInformation());
        System.out.println(dossier);
        Dossier d = this.dossierRepository.save(dossier);
        System.out.println(d);
        p.setDossier(d);
        System.out.println(p);
        return new ResponseEntity<>(patientRepository.save(p), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(patientRepository.findAll(), HttpStatus.OK);
    }
}
