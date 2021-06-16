package com.pharma.patientrecords.repositories;

import com.pharma.patientrecords.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findTop5ByFirstNameContainsOrLastNameContains(String firstname, String lastname);
    List<Patient> findTop5ByFirstNameContainsAndLastNameContains(String firstname, String lastname);
    ArrayList<Patient> findTop5ByFirstNameIn(Collection<String> firstname);
}
