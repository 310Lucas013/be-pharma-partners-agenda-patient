package com.pharma.patientrecords.service;

import com.pharma.patientrecords.models.Patient;
import com.pharma.patientrecords.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> findTop5ByFirstNameContainsAndLastNameContains(String firstName, String lastName) {
        return patientRepository.findTop5ByFirstNameContainsAndLastNameContains(firstName,lastName);
    }

    public List<Patient> findTop5ByFirstNameContainsOrLastNameContains(String name, String name1) {
        return  patientRepository.findTop5ByFirstNameContainsOrLastNameContains(name,name1);
    }

    public Object save(Patient p) {
        return patientRepository.save(p);
    }

    public Object findAll() {
        return patientRepository.findAll();
    }

    public Optional<Patient> findById(long id) {
        return patientRepository.findById(id);
    }
}
