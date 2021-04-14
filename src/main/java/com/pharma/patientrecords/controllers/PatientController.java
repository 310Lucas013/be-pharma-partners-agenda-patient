package com.pharma.patientrecords.controllers;

import com.google.gson.Gson;
import com.pharma.patientrecords.messaging.CreateLocationMessage;
import com.pharma.patientrecords.models.Dossier;
import com.pharma.patientrecords.models.Patient;
import com.pharma.patientrecords.models.dto.PatientDto;
import com.pharma.patientrecords.models.enums.Gender;
import com.pharma.patientrecords.repositories.DossierRepository;
import com.pharma.patientrecords.repositories.PatientRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/patients")
public class PatientController {
    private final PatientRepository patientRepository;
    private final DossierRepository dossierRepository;
    private final AmqpTemplate rabbitTemplate;
    private final Gson gson;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routingKey}")
    private String routingkey;

    public PatientController(PatientRepository patientRepository, DossierRepository dossierRepository,
                             AmqpTemplate rabbitTemplate, Gson gson) {
        this.patientRepository = patientRepository;
        this.dossierRepository = dossierRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.gson = gson;

    }

    @PostMapping()
    public ResponseEntity<?> savePatient(@RequestBody PatientDto patientDto) {
        Patient p = new Patient();
        p.setFirstName(patientDto.getFirstName());
        p.setLastName(patientDto.getLastName());
        p.setMiddleName(patientDto.getMiddleName());
        p.setGender(patientDto.getGender());
        p.setDateOfBirth(patientDto.getDateOfBirth());
        p.setPhoneNumber(patientDto.getPhoneNumber());
        Dossier dossier = new Dossier(patientDto.getDossierInformation());
        Dossier d = this.dossierRepository.save(dossier);
        p.setDossier(d);
        CreateLocationMessage clm = new CreateLocationMessage();
        clm.setStreetName(patientDto.getStreetName());
        clm.setHouseNumber(patientDto.getHouseNumber());
        clm.setZipCode(patientDto.getZipCode());
        clm.setCity(patientDto.getCity());
        clm.setCountry(patientDto.getCountry());
        long locationId = this.saveLocation(clm);
        System.out.println(locationId);
        p.setLocationId(locationId);
        return new ResponseEntity<>(patientRepository.save(p), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(patientRepository.findAll(), HttpStatus.OK);
    }

    public long saveLocation(CreateLocationMessage clm) {
        String result = gson.toJson(clm);
        rabbitTemplate.convertAndSend(exchange, "create-location", result);
        System.out.println("test");
        return 1;
    }


}
