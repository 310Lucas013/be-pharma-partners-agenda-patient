package com.pharma.patientrecords.messaging;

import lombok.Data;

import java.io.Serializable;

@Data
public class CreateLocationMessage implements Serializable {
    private String streetName;
    private String houseNumber;
    private String zipCode;
    private String city;
    private String country;
}
