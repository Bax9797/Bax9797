package pl.kurs.java.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.java.test.model.ModelPatientToAdd;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PatientRestControllerIT {

    @LocalServerPort
    private int serverPort;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    private URI createServerAddress() throws URISyntaxException {
        return new URI("http://localhost:" + serverPort + "/patient");
    }

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldAddPatient() throws Exception {
        ModelPatientToAdd underTest = new ModelPatientToAdd("Kapsel", "Pies", "Pitbull",
                4, "Michał", "Piec", "mkrolak97797@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void shouldNotAddPatientAndThrowExceptionAgeAnimalNegativeException() throws Exception {
        ModelPatientToAdd underTest = new ModelPatientToAdd("Kapsel", "Pies", "Pitbull",
                -4, "Michał", "Piec", "mkrolak97433797@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "{\"message\":\"Validation Failed\",\"details\":[\"animal age cannot be negative\"]}"));
    }

    @Test
    void shouldNotAddPatientAndThrowEmptyFieldsException() throws Exception {
        ModelPatientToAdd underTest = new ModelPatientToAdd("", "pies", "Pitbull",
                4, "Michał", "Kot", "mkrolak97797@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Validation Failed\",\"details\":[\"field animal name must be not empty\"]}"));
    }

    @Test
    void shouldNotAddPatientAndThrowDuplicateEmailException() throws Exception {
        ModelPatientToAdd underTest = new ModelPatientToAdd("Kapsel", "Pies", "Pitbull",
                4, "Michał", "Piec", "mts@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "{\"message\":\"Validation Failed\",\"details\":[\"Email is already taken\"]}"));
    }

    @Test
    void shouldGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string(
                        "{\"id\":1,\"animalName\":\"Kapsel\",\"animalSpecies\":\"pies\"," +
                                "\"animalBreed\":\"dog\",\"age\":5,\"ownerName\":\"Igor\",\"ownerSurname\":\"Kot\"," +
                                "\"email\":\"mts@gmail.com\"}"));
    }

    @Test
    void shouldNotGetByIdAndThrowsNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("User Not Found with id : 500"));
    }

    @Test
    void shouldRemoveFromTheListOfCurrentPatients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(createServerAddress() + "/1/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"changed status of given" +
                        " patient, this client is no longer our patient\"}"));
    }

    @Test
    void shouldNotRemoveFromTheListOfCurrentPatientsAndThrowsNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(createServerAddress() + "/1111/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("User Not Found with id : 1111"));
    }

    @Test
    void shouldReturnPageOfPatient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}