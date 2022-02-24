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
@ActiveProfiles("dev")
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
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":4,\"name\":\"Michal\"," +
                        "\"surname\":\"Kot\",\"medicalSpecialization\":\"Kardiolog\",\"animalSpecialization\":\"Psy\"," +
                        "\"rate\":100.0,\"nip\":\"3523532532\",\"hired\":true}"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsSalaryNegativeException() throws Exception {
        ModelDoctorToAdd underTest = new ModelDoctorToAdd("Michal", "Kot", "Kardiolog",
                "Psy", -100.00, "3523532532");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"BAD_REQUEST\"," +
                        "\"status\":400,\"message\":\"salary cannot be negative\"}"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsDuplicateNipException() throws Exception {
        ModelDoctorToAdd underTest = new ModelDoctorToAdd("Michal", "Kot", "Kardiolog",
                "Psy", 100.00, "864232112");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"BAD_REQUEST\"," +
                        "\"status\":400,\"message\":\"duplicate tax identification number. " +
                        "There is a person with the given tax identification number in the database\"}"));
    }

    @Test
    void shouldNotAddDoctorAndThrowsEmptyFieldsException() throws Exception {
        ModelDoctorToAdd underTest = new ModelDoctorToAdd("", "", "Kardiolog",
                "Psy", 100.00, "864232112222");
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"BAD_REQUEST\"," +
                        "\"status\":400,\"message\":\"" +
                        "all fields must be not empty or the rate was entered incorrectly\"}"));
    }

    @Test
    void shouldGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"MichaÅ\u0082\"," +
                        "\"surname\":\"Gocek\",\"medicalSpecialization\":\"Kardiolog\"," +
                        "\"animalSpecialization\":\"Pies\",\"rate\":100.0,\"nip\":\"864232112\",\"hired\":true}"));
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
    void shouldDismissTheEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(createServerAddress() + "/fire/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("changed status of given doctor," +
                        " this doctor will not be able to handle any visits"));
    }

    @Test
    void shouldNotDismissTheEmployeeThrowDoctorNotFoundExceptions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(createServerAddress() + "/fire/4221")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"NOT_FOUND\"," +
                        "\"status\":404,\"message\":\"User Not Found with id : 4221\"}"));
    }

    @Test
    void shouldReturnPageOfDoctor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}