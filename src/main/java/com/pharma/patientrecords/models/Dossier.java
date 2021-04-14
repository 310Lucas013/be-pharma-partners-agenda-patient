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
    @Column(columnDefinition = "nvarchar(max)")
    private String information;
    @OneToOne(mappedBy = "dossier")
    private Patient patient;

    public Dossier() {

    }

    public Dossier(String information) {
        this.information = information;
    }
}
