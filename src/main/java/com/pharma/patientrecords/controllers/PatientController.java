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
import com.pharma.patientrecords.repositories.DossierRepository;
import com.pharma.patientrecords.repositories.PatientRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Modifier;
import java.util.*;

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

    @GetMapping("findByName/{name}")
    public ResponseEntity<?> getPatientByName(@PathVariable(value="name") String name)  {
        List<Patient> patients = null;
        if (name.contains(" ")) {
            patients = getPatientsByMultipleInput(name);
        } else {
            patients = getPatientBySingleInput(name);
        }
        String result = gson.toJson(patients);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    private List<Patient> getPatientsByMultipleInput(String name) {
        List<Patient> patients = null;
        String[] names = name.split(" ");
        String firstName = names[0];
        int lastNameNumber = names.length - 1;
        String lastName = names[lastNameNumber];
        if (firstName.equals("")) {
            // firstname equals empty string
            if (lastName.equals("")) {
                // both have empty string
                return patients;
            } else {
                // only firstname is empty string
                patients = getPatientBySingleInput(lastName);
            }
        } else if (lastName.equals("")) {
            // only lastname is empty string
            patients = getPatientBySingleInput(firstName);
        } else {
            // firstname and lastname are both NOT empty string
            List<Patient> tempPatients = patientRepository.findTop5ByFirstNameContainsAndLastNameContains(firstName, lastName);
            tempPatients.addAll(patientRepository.findTop5ByFirstNameContainsAndLastNameContains(lastName, firstName));
            patients = new ArrayList<>(
                    new HashSet<>(tempPatients));
        }
        return patients;
    }

    private List<Patient> getPatientBySingleInput(String name) {
        return patientRepository.findTop5ByFirstNameContainsOrLastNameContains(name, name);
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

    @GetMapping(path="/getAll")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(patientRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id)  {
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isEmpty()){
            return new ResponseEntity<>(gson.toJson(new Patient()), HttpStatus.OK);
        }
        return new ResponseEntity<>(gson.toJson(patient.get()), HttpStatus.OK);
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

    @GetMapping()
    public ResponseEntity<?> findByUs() {
        return new ResponseEntity<>(patientRepository.findAll(), HttpStatus.OK);
    }

}
