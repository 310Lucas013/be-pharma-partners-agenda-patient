package com.pharma.patientrecords;

import com.google.gson.Gson;
import com.pharma.patientrecords.controllers.PatientController;
import com.pharma.patientrecords.models.Dossier;
import com.pharma.patientrecords.models.Patient;
import com.pharma.patientrecords.models.enums.Gender;
import com.pharma.patientrecords.repositories.DossierRepository;
import com.pharma.patientrecords.repositories.PatientRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PatientRecordsApplication.class)
@WebMvcTest(PatientController.class)
class PatientRecordsApplicationTests {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private PatientRepository patientRepository;
    @MockBean
    private DossierRepository dossierRepository;

    private final Gson gson = new Gson();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void contextLoads() {
        assertThat(patientRepository).isNotNull();
        assertThat(dossierRepository).isNotNull();
    }


    @Test
    void GetAll() throws Exception {
        Dossier dossier1 = new Dossier();
        Dossier dossier2 = new Dossier();
        Patient patient1 = new Patient((long) 1, "tessa", "Huize", " ", Gender.Vrouw, new Date(), "", dossier1);
        Patient patient2 = new Patient((long) 2, "harrold", "Huize", " ", Gender.Man, new Date(), "", dossier2);
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient1);
        patientList.add(patient2);

        given(patientRepository.findAll()).willReturn(patientList);
        mvc.perform(MockMvcRequestBuilders
                .get("/patients/getAll")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }
}


