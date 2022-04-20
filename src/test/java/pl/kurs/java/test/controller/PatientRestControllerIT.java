package pl.kurs.java.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.java.test.model.CreatePatientRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PatientRestControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldAddPatient() throws Exception {
        CreatePatientRequest underTest = new CreatePatientRequest("Kapsel", "Pies", "Pitbull",
                4, "Michał", "Piec", "mkrolak97797@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.animalName").value("Kapsel"))
                .andExpect(jsonPath("$.animalSpecies").value("Pies"))
                .andExpect(jsonPath("$.animalBreed").value("Pitbull"))
                .andExpect(jsonPath("$.age").value("4"))
                .andExpect(jsonPath("$.ownerName").value("Michał"))
                .andExpect(jsonPath("$.ownerSurname").value("Piec"))
                .andExpect(jsonPath("$.email").value("mkrolak97797@gmail.com"));
    }

    @Test
    void shouldNotAddPatientAndThrowDuplicateEmailException() throws Exception {
        CreatePatientRequest underTest = new CreatePatientRequest("Kapsel", "Pies", "Pitbull",
                4, "Michał", "Piec", "15@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("email"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("ERROR_ENTITY_EXISTS"));
    }

    @Test
    void shouldNotAddPatientAndThrowExceptionAgeAnimalNegativeException() throws Exception {
        CreatePatientRequest underTest = new CreatePatientRequest("Kapsel", "Pies", "Pitbull",
                -4, "Michał", "Piec", "mkrolak97433797@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("age"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("animal age cannot be negative"));
    }

    @Test
    void shouldNotAddPatientAndThrowEmptyFieldsException() throws Exception {
        CreatePatientRequest underTest = new CreatePatientRequest("", "pies", "Pitbull",
                4, "Michał", "Kot", "mkrolak97797@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("animalName"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("field animal name must be " +
                        "not empty"));
    }

    @Test
    void shouldGetById() throws Exception {
        mockMvc.perform(get("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void shouldNotGetByIdAndThrowsNotFoundException() throws Exception {
        mockMvc.perform(get("/patient/500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ENTITY_NOT_FOUND"))
                .andExpect(jsonPath("$.entityName").value("patient"))
                .andExpect(jsonPath("$.id").value("500"));
    }

    @Test
    void shouldRemoveFromTheListOfCurrentPatients() throws Exception {
        mockMvc.perform(put("/patient/2/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(
                        "delete"));
    }

    @Test
    void shouldNotRemoveFromTheListOfCurrentPatientsAndThrowsNotFoundException() throws Exception {
        mockMvc.perform(put("/patient/1111/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ENTITY_NOT_FOUND"))
                .andExpect(jsonPath("$.entityName").value("patient"))
                .andExpect(jsonPath("$.id").value("1111"));;
    }

    @Test
    void shouldReturnPageOfPatient() throws Exception {
        mockMvc.perform(get("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }
}