package pl.kurs.java.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.java.test.mail.EmailService;
import pl.kurs.java.test.model.ModelToFindNearestVisit;
import pl.kurs.java.test.model.ModelVisitToAdd;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class VisitRestControllerTestIT {

    @LocalServerPort
    private int serverPort;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmailService emailService;

    private URI createServerAddress() throws URISyntaxException {
        return new URI("http://localhost:" + serverPort + "/visit");
    }

    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldGetVisit() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        ModelVisitToAdd underTest = new ModelVisitToAdd(1, 1,
                LocalDateTime.of(2022, Month.APRIL, 3, 6, 00, 00));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("3"));
    }

    @Test
    void shouldGetVisitAndThrowsNotFoundDoctor() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        ModelVisitToAdd underTest = new ModelVisitToAdd(1500, 1,
                LocalDateTime.of(2022, Month.APRIL, 3, 6, 00, 00));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "Doctor Not Found with id : 1500"));
    }

    @Test
    void shouldGetVisitAndThrowsNotFoundPatient() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        ModelVisitToAdd underTest = new ModelVisitToAdd(1, 1500,
                LocalDateTime.of(2022, Month.APRIL, 3, 6, 00, 00));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "Patient Not Found with id : 1500"));
    }

    @Test
    void shouldGetVisitAndThrowsValidDate() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        ModelVisitToAdd underTest = new ModelVisitToAdd(1, 1,
                LocalDateTime.of(2017, Month.APRIL, 3, 6, 00, 00));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "{\"message\":\"Validation Failed\",\"details\":[\" date must be in the future\"]}"));
    }

    @Test
    void shouldConfirmVisit() throws Exception {
        String content = objectMapper.writeValueAsString("token12345");
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/token12345/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"visit confirmed!\"}"));
    }

    @Test
    void shouldNotConfirmVisitAndThrowsTokenNotFoundException() throws Exception {
        String content = objectMapper.writeValueAsString("tokenNotFound");
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/tokenNotFound/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("There is no Token with the given parameters"));
    }

    @Test
    void shouldCancelVisit() throws Exception {
        String content = objectMapper.writeValueAsString("token1");
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/token1/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"the visit has been canceled\"}"));
    }

    @Test
    void shouldNotCancelVisitAndThrowsTokenNotFoundException() throws Exception {
        String content = objectMapper.writeValueAsString("tokenNotFound");
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/tokenNotFound/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("There is no Token with the given parameters"));
    }

    @Test
    void shouldFindTopNearestVisits() throws Exception {
        ModelToFindNearestVisit underTest = new ModelToFindNearestVisit("kardiolog", "pies"
                , LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(5L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void shouldNotFindTopNearestVisitsAndThrowsNotFoundDoctorWithTheGivenParametersOfSpecializationsException() throws Exception {
        ModelToFindNearestVisit underTest = new ModelToFindNearestVisit("Test", "Test"
                , LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "There is no doctor with the given parameters of Specialization "));
    }

    @Test
    void shouldNotFindTopNearestVisitsAndThrowsNotFoundFreeVisitAtGivenTimeException() throws Exception {
        ModelToFindNearestVisit underTest = new ModelToFindNearestVisit("Kardiolog", "Pies"
                , LocalDateTime.of(2022, Month.JULY, 15, 16, 00, 00), LocalDateTime.of(2022, Month.JULY, 15, 16, 00, 00));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "There is no free visit at given time, please change the time slot for the meeting"));
    }
}