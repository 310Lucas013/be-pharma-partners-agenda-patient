package com.pharma.patientrecords.models.dto;

import com.pharma.patientrecords.models.enums.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class PatientDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private Date dateOfBirth;
    private String phoneNumber;
    private String dossierInformation;
    private String streetName;
    private String houseNumber;
    private String zipCode;
    private String city;
    private String country;

}
