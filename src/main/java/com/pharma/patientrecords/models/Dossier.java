package com.pharma.patientrecords.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table
public class Dossier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "nvarchar(max)")
    private String information;
    @JsonIgnore
    @OneToOne(mappedBy = "dossier")
    private Patient patient;

    public Dossier() {

    }

    public Dossier(String information) {
        this.information = information;
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
