package com.pharma.patientrecords;

import com.google.gson.Gson;
import com.pharma.patientrecords.config.JwtAuthenticationEntryPoint;
import com.pharma.patientrecords.config.JwtTokenUtil;
import com.pharma.patientrecords.config.WebSecurityConfiguration;
import com.pharma.patientrecords.controllers.PatientController;
import com.pharma.patientrecords.models.Dossier;
import com.pharma.patientrecords.models.Patient;
import com.pharma.patientrecords.models.dto.PatientDto;
import com.pharma.patientrecords.models.enums.Gender;
import com.pharma.patientrecords.service.DossierService;
import com.pharma.patientrecords.service.PatientService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.xml.stream.Location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@ContextConfiguration(classes = PatientRecordsApplication.class)
@WebMvcTest(PatientController.class)
class PatientRecordsApplicationTests {


    @Autowired
    private MockMvc mvc;
    @MockBean
    private AmqpTemplate rabbitTemplate;
    @MockBean
    private PatientService patientService;
    @MockBean
    private DossierService dossierService;
    @Autowired
    private WebSecurityConfiguration webSecurityConfiguration;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void contextLoads() {
        assertThat(patientService).isNotNull();
        assertThat(dossierService).isNotNull();
        assertThat(webSecurityConfiguration).isNotNull();
        assertThat(jwtAuthenticationEntryPoint).isNotNull();
        assertThat(jwtTokenUtil).isNotNull();
    }


    @Test
    @WithMockUser(username = "admin" , authorities = "admin")
    void GetAll() throws Exception {
        Dossier dossier1 = new Dossier();
        Dossier dossier2 = new Dossier();
        Patient patient1 = new Patient((long) 1, "tessa", "Huize", " ", Gender.Vrouw, new Date(), "", dossier1);
        Patient patient2 = new Patient((long) 2, "harrold", "Huize", " ", Gender.Man, new Date(), "", dossier2);
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        given(patientService.findAll()).willReturn(patientList);
        mvc.perform(MockMvcRequestBuilders
                .get("/patients/getAll")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "admin" , authorities = "admin")
    void getById() throws Exception {
        Dossier dossier1 = new Dossier();
        Dossier dossier2 = new Dossier();
        Patient patient1 = new Patient((long) 1, "tessa", "Huize", " ", Gender.Vrouw, new Date(), "", dossier1);
        Patient patient2 = new Patient((long) 2, "harrold", "Huize", " ", Gender.Man, new Date(), "", dossier2);
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        given(patientService.findById(1)).willReturn(java.util.Optional.of(patient1));
        mvc.perform(MockMvcRequestBuilders
                .get("/patients/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
    }

//    @Test
//    @WithMockUser(username = "admin" , authorities = "admin")
//    void savePatient() throws Exception {
//
//        Dossier dossier1 = new Dossier();
//        Patient patient1 = new Patient((long) 1, "tessa", "Huize", " ", Gender.Vrouw, new Date(), "", dossier1);
//        PatientDto p = new PatientDto();
//        p.setFirstName(patient1.getFirstName());
//        p.setLastName(patient1.getLastName());
//        p.setMiddleName(patient1.getMiddleName());
//        p.setGender(patient1.getGender());
//        p.setDateOfBirth(patient1.getDateOfBirth());
//        p.setPhoneNumber(patient1.getPhoneNumber());
//        p.setDossierInformation("");
//        p.setStreetName("Street");
//        p.setHouseNumber("HouseNumber");
//        p.setZipCode("Zipcode");
//        p.setCity("City");
//        p.setCountry("Country");
//        given(patientService.save(any())).willReturn(patient1);
//        given(dossierService.save(any())).willReturn(dossier1);
//        mvc.perform(MockMvcRequestBuilders
//                .post("/patients", gson.toJson(p))
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").exists());
//    }
    @Test
    @WithMockUser(username = "admin" , authorities = "admin")
    void getPatientByName() throws Exception {
        Dossier dossier1 = new Dossier();
        Patient patient1 = new Patient((long) 1, "tessa", "Huize", " ", Gender.Vrouw, new Date(), "", dossier1);

        given(patientService.findTop5ByFirstNameContainsOrLastNameContains("tessa","tessa")).willReturn(Collections.singletonList(patient1));
        mvc.perform(MockMvcRequestBuilders
                .get("/patients/findByName/tessa")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }



}


