package com.pharma.patientrecords.models;

import javax.persistence.*;

@Entity
@Table
public class Dossier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String information;

    @OneToOne
    private Patient patient;

    public Dossier() {

    }

    public Dossier(Long id, String information, Patient patient) {
        this.id = id;
        this.information = information;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
