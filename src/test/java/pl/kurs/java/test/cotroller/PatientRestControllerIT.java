package pl.kurs.java.test.cotroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.java.test.entity.Patient;
import pl.kurs.java.test.model.ModelPatientToAdd;
import pl.kurs.java.test.repository.PatientRepository;

import javax.servlet.ServletContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
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
                -4, "Michał", "Piec", "mkrolak97797@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"BAD_REQUEST\",\"status" +
                        "\":400,\"message\":\"animal age can not be less than 0\"}"));
    }

    @Test
    void shouldNotAddPatientAndThrowEmptyFieldsException() throws Exception {
        ModelPatientToAdd underTest = new ModelPatientToAdd("", "", "Pitbull",
                4, "Michał", "", "mkrolak97797@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"BAD_REQUEST\",\"status" +
                        "\":400,\"message\":\"all fields must be not empty\"}"));
    }

    @Test
    void shouldNotAddPatientAndThrowDuplicateEmailException() throws Exception {
        ModelPatientToAdd underTest = new ModelPatientToAdd("Kapsel", "Pies", "Pitbull",
                4, "Michał", "Piec", "mado@gmail.com");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"BAD_REQUEST\",\"status" +
                        "\":400,\"message\":\"Email has to be unique. There is a person with the given email in the database\"}"));
    }

    @Test
    void shouldGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"animalName\":\"Kapsel\"," +
                        "\"animalSpecies\":\"Pies\",\"animalBreed\":\"Pitbull\",\"age\":4,\"ownerName\":\"MichaÅ\u0082\"," +
                        "\"ownerSurname\":\"Piec\",\"email\":\"mkrolak9797@gmail.com\",\"currentCustomer\":true}"));
    }

    @Test
    void shouldNotGetByIdAndThrowsNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"NOT_FOUND\"," +
                        "\"status\":404,\"message\":\"User Not Found with id : 500\"}"));
    }

    @Test
    void shouldRemoveFromTheListOfCurrentPatients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(createServerAddress() + "/remove/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("changed status of given patient," +
                        " this client is no longer our patient"));
    }

    @Test
    void shouldNotRemoveFromTheListOfCurrentPatientsAndThrowsNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(createServerAddress() + "/remove/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"NOT_FOUND\"," +
                        "\"status\":404,\"message\":\"User Not Found with id : 1000\"}"));
    }

    @Test
    void shouldReturnPageOfPatient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}