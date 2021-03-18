package com.pharma.patientrecords.models;

import com.pharma.patientrecords.models.enums.Gender;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String middleName;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @Column
    private Date dateOfBirth;
    @Column
    private String phoneNumber;
    @OneToOne
    @JoinColumn(name = "dossier_id", referencedColumnName = "id", nullable = false)
    private Dossier dossier;
    @Column(name = "location_id")
    private long locationId;

    public Patient() {
    }

    public Patient(Long id, String firstName, String lastName, String middleName, Gender gender, Date dateOfBirth, String phoneNumber, Dossier dossier) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.dossier = dossier;
    }
}
