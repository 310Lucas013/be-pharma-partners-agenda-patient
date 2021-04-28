package com.pharma.patientrecords.models.dto;

import com.pharma.patientrecords.models.enums.Gender;

import java.util.Date;

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

    public PatientDto() {
    }

    public PatientDto(String firstName, String lastName, String middleName, Gender gender, Date dateOfBirth, String phoneNumber, String dossierInformation, String streetName, String houseNumber, String zipCode, String city, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.dossierInformation = dossierInformation;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDossierInformation() {
        return dossierInformation;
    }

    public void setDossierInformation(String dossierInformation) {
        this.dossierInformation = dossierInformation;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
