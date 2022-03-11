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
import pl.kurs.java.test.model.ModelDoctorToAdd;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DoctorRestControllerTestIT {

    @LocalServerPort
    private int serverPort;
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

    private URI createServerAddress() throws URISyntaxException {
        return new URI("http://localhost:" + serverPort + "/doctor");
    }

    @Test
    void shouldAddDoctor() throws Exception {
        ModelDoctorToAdd underTest = new ModelDoctorToAdd("Michal", "Kot", "Kardiolog",
                "Psy", 100.00, "3523532532");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":2,\"name\":\"Michal\"," +
                        "\"surname\":\"Kot\",\"medicalSpecialization\":\"Kardiolog\",\"animalSpecialization\":\"Psy\"," +
                        "\"rate\":100.0,\"nip\":\"3523532532\"}"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsSalaryNegativeException() throws Exception {
        ModelDoctorToAdd underTest = new ModelDoctorToAdd("Michal", "Kot", "Kardiolog",
                "Psy", -100.00, "35123532532");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Validation Failed\"," +
                        "\"details\":[\"rate cannot be negative\"]}"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsDuplicateNipException() throws Exception {
        ModelDoctorToAdd underTest = new ModelDoctorToAdd("Michal", "Kot", "Kardiolog",
                "Psy", 100.00, "54366732");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Validation Failed\"," +
                        "\"details\":[\"Nip is already taken\"]}"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsEmptyFieldsException() throws Exception {
        ModelDoctorToAdd underTest = new ModelDoctorToAdd("", "Kowal", "Kardiolog",
                "Psy", 100.00, "864232112222");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Validation Failed\"," +
                        "\"details\":[\"field name must be not empty\"]}"));
    }

    @Test
    void shouldGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Tomasz\"," +
                        "\"surname\":\"Kot\",\"medicalSpecialization\":\"kardiolog\",\"animalSpecialization\":\"pies\"," +
                        "\"rate\":100.0,\"nip\":\"54366732\"}"));
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
    void shouldDismissTheEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(createServerAddress() + "/1/fire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"changed status of given " +
                        "doctor, this doctor will not be able to handle any visits\"}"));
    }

    @Test
    void shouldNotDismissTheEmployeeThrowDoctorNotFoundExceptions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(createServerAddress() + "/500/fire")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("User Not Found with id : 500"));
    }

    @Test
    void shouldReturnPageOfDoctor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}