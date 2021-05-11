package com.pharma.patientrecords.controllers;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pharma.patientrecords.messaging.CreateLocationMessage;
import com.pharma.patientrecords.models.Dossier;
import com.pharma.patientrecords.models.HibernateProxyTypeAdapter;
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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
                             AmqpTemplate rabbitTemplate) {
        this.patientRepository = patientRepository;
        this.dossierRepository = dossierRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.gson = initiateGson();

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
        // Save patient and get ID.
        // Set patient id in Create locationMessage.
        // Save location and get the id.
        // Send RabbitMQ Message from Location to Patient with location and patient id.
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
        System.out.println(result);
        rabbitTemplate.convertAndSend(exchange, "create-location", clm);
        System.out.println("test");
        return 1;
    }


    private Gson initiateGson() {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        boolean exclude = false;
                        try {
                            exclude = EXCLUDE.contains(f.getName());
                        } catch (Exception ignore) {
                        }
                        return exclude;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                });
        return b.create();
    }

    private static final List<String> EXCLUDE = new ArrayList<>() {{
        add("patient");
    }};


}
