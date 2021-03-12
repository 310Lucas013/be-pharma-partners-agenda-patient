package com.pharma.patientrecords.repositories;

import com.pharma.patientrecords.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
