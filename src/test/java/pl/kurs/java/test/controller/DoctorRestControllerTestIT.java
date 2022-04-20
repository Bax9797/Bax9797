package pl.kurs.java.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import pl.kurs.java.test.model.CreateDoctorRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@RequiredArgsConstructor
class DoctorRestControllerTestIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldAddDoctor() throws Exception {
        CreateDoctorRequest underTest = new CreateDoctorRequest("Michal", "Kot", "Kardiolog",
                "Psy", 100.0, "3523532532");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Michal"))
                .andExpect(jsonPath("$.surname").value("Kot"))
                .andExpect(jsonPath("$.medicalSpecialization").value("Kardiolog"))
                .andExpect(jsonPath("$.animalSpecialization").value("Psy"))
                .andExpect(jsonPath("$.rate").value("100.0"))
                .andExpect(jsonPath("$.nip").value("3523532532"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsSalaryNegativeException() throws Exception {
        CreateDoctorRequest underTest = new CreateDoctorRequest("Michal", "Kot", "Kardiolog",
                "Psy", -100.00, "35123532532");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("rate"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("rate cannot be negative"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsDuplicateNipException() throws Exception {
        CreateDoctorRequest underTest = new CreateDoctorRequest("Michal", "Kot", "Kardiolog",
                "Psy", 100.00, "54366732");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("nip"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("ERROR_NIP_EXISTS"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsEmptyFieldsException() throws Exception {
        CreateDoctorRequest underTest = new CreateDoctorRequest("", "Kowal", "Kardiolog",
                "Psy", 100.00, "864232112222");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("field name must be not empty"));
    }

    @Test
    void shouldGetById() throws Exception {
        mockMvc.perform(get("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void shouldNotGetByIdAndThrowsNotFoundException() throws Exception {
        mockMvc.perform(get("/doctor/500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ENTITY_NOT_FOUND"))
                .andExpect(jsonPath("$.entityName").value("doctor"))
                .andExpect(jsonPath("$.id").value("500"));
    }

    @Test
    void shouldDismissTheEmployee() throws Exception {
        mockMvc.perform(put("/doctor/2/fire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(
                        "dismiss"));
    }

    @Test
    void shouldNotDismissTheEmployeeThrowDoctorNotFoundExceptions() throws Exception {
        mockMvc.perform(put("/doctor/500/fire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("ENTITY_NOT_FOUND"))
                .andExpect(jsonPath("$.entityName").value("doctor"))
                .andExpect(jsonPath("$.id").value("500"));

    }

    @Test
    void shouldReturnPageOfDoctor() throws Exception {
        mockMvc.perform(get("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.paged").value("true"));
    }
}