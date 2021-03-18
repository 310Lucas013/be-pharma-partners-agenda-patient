package com.pharma.patientrecords.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Dossier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String information;
    @OneToOne(mappedBy = "dossier")
    private Patient patient;

    public Dossier() {

    }

    public Dossier(Long id, String information, Patient patient) {
        this.id = id;
        this.information = information;
        this.patient = patient;
    }
}
