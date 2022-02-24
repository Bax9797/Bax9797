package pl.kurs.java.test.cotroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import pl.kurs.java.test.model.ModelToAddVisit;
import pl.kurs.java.test.model.ModelToFindNearestVisit;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@RequiredArgsConstructor
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
        doNothing().when(emailService).sendSimpleMessage(any(), any(), any());
        ModelToAddVisit underTest = new ModelToAddVisit(1, 1,
                LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 00, 00));
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
        doNothing().when(emailService).sendSimpleMessage(any(), any(), any());
        ModelToAddVisit underTest = new ModelToAddVisit(1500, 1,
                LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 00, 00));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "{\"httpStatus\":\"NOT_FOUND\",\"status\":404,\"message\":" +
                                "\"User Not Found with id : 1500\"}"));
    }

    @Test
    void shouldGetVisitAndThrowsNotFoundPatient() throws Exception {
        doNothing().when(emailService).sendSimpleMessage(any(), any(), any());
        ModelToAddVisit underTest = new ModelToAddVisit(1500, 1,
                LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 00, 00));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string(
                        "{\"httpStatus\":\"NOT_FOUND\",\"status\":404,\"message\":" +
                                "\"User Not Found with id : 1500\"}"));
    }

    @Test
    void shouldConfirmVisit() throws Exception {
        String content = objectMapper.writeValueAsString("token12345");
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/confirm/token12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("visit confirmed!"));
    }

    @Test
    void shouldNotConfirmVisitAndThrowsTokenNotFoundException() throws Exception {
        String content = objectMapper.writeValueAsString("tokenNotFound");
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/confirm/tokenNotFound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"NOT_FOUND\"," +
                        "\"status\":404,\"message\":\"There is no Token with the given parameters\"}"));
    }

    @Test
    void shouldCancelVisit() throws Exception {
        String content = objectMapper.writeValueAsString("token1");
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/cancel/token1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string("the visit has been canceled"));
    }

    @Test
    void shouldNotCancelVisitAndThrowsTokenNotFoundException() throws Exception {
        String content = objectMapper.writeValueAsString("tokenNotFound");
        mockMvc.perform(MockMvcRequestBuilders.get(createServerAddress() + "/confirm/tokenNotFound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"NOT_FOUND\"," +
                        "\"status\":404,\"message\":\"There is no Token with the given parameters\"}"));
    }

    @Test
    void shouldFindTopNearestVisits() throws Exception {
        ModelToFindNearestVisit underTest = new ModelToFindNearestVisit("Kardiolog", "Pies"
                , LocalDateTime.now().minusHours(2L), LocalDateTime.now().plusHours(2L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void shouldNotFindTopNearestVisitsAndThrowsNotFoundDoctorWithTheGivenParametersOfSpecializationsException() throws Exception {
        ModelToFindNearestVisit underTest = new ModelToFindNearestVisit("Urolog", "Konie"
                , LocalDateTime.now().minusHours(2L), LocalDateTime.now().plusHours(2L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"NOT_FOUND\"," +
                        "\"status\":404,\"message\":" +
                        "\"There is no doctor with the given parameters of Specialization \"}"));
    }

    @Test
    void shouldNotFindTopNearestVisitsAndThrowsNotFoundFreeVisitAtGivenTimeException() throws Exception {
        ModelToFindNearestVisit underTest = new ModelToFindNearestVisit("Kardiolog", "Psy"
                , LocalDateTime.now(), LocalDateTime.now());
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(MockMvcRequestBuilders.post(createServerAddress() + "/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().string("{\"httpStatus\":\"NOT_FOUND\"," +
                        "\"status\":404,\"message\":" +
                        "\"There is no doctor with the given parameters of Specialization \"}"));
    }
}