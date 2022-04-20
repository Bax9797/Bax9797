package pl.kurs.java.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import pl.kurs.java.test.dto.VisitIdDto;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.entity.Patient;
import pl.kurs.java.test.entity.Token;
import pl.kurs.java.test.entity.Visit;
import pl.kurs.java.test.mail.EmailService;
import pl.kurs.java.test.model.CreateDoctorRequest;
import pl.kurs.java.test.model.CreatePatientRequest;
import pl.kurs.java.test.model.FindVisitsRequest;
import pl.kurs.java.test.model.VisitToAddRequest;
import pl.kurs.java.test.repository.DoctorRepository;
import pl.kurs.java.test.repository.PatientRepository;
import pl.kurs.java.test.repository.TokenRepository;
import pl.kurs.java.test.repository.VisitRepository;
import pl.kurs.java.test.service.visit.VisitService;

import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class VisitRestControllerTestIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmailService emailService;
    @Autowired
    private VisitRepository visitRepository;
    private VisitService visitService;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private TokenRepository tokenGeneratorRepository;


    @BeforeEach
    void setUp() {
        visitService = new VisitService(
                visitRepository, patientRepository, doctorRepository, tokenGeneratorRepository, emailService);
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testFullApplication() throws Exception {
        CreatePatientRequest underTestPatientRequest = new CreatePatientRequest("Kapsel", "Pies", "Pitbull",
                4, "Michał", "Piec", "mkrolak997@gmail.com");
        String contentPatient = objectMapper.writeValueAsString(underTestPatientRequest);
        MvcResult mvcResultPatient = mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentPatient)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.animalName").value("Kapsel"))
                .andExpect(jsonPath("$.animalSpecies").value("Pies"))
                .andExpect(jsonPath("$.animalBreed").value("Pitbull"))
                .andExpect(jsonPath("$.age").value("4"))
                .andExpect(jsonPath("$.ownerName").value("Michał"))
                .andExpect(jsonPath("$.ownerSurname").value("Piec"))
                .andExpect(jsonPath("$.email").value("mkrolak997@gmail.com"))
                .andReturn();

        CreateDoctorRequest underTestDoctorRequest = new CreateDoctorRequest("Ewa", "Pionek", "Kardiolog",
                "Psy", 100.0, "35235325325232");
        String contentDoctor = objectMapper.writeValueAsString(underTestDoctorRequest);
        MvcResult mvcResultDoctor = mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentDoctor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ewa"))
                .andExpect(jsonPath("$.surname").value("Pionek"))
                .andExpect(jsonPath("$.medicalSpecialization").value("Kardiolog"))
                .andExpect(jsonPath("$.animalSpecialization").value("Psy"))
                .andExpect(jsonPath("$.rate").value("100.0"))
                .andExpect(jsonPath("$.nip").value("35235325325232"))
                .andReturn();

        Doctor doctor = objectMapper.readValue(mvcResultDoctor.getResponse().getContentAsString(), Doctor.class);
        Patient patient = objectMapper.readValue(mvcResultPatient.getResponse().getContentAsString(), Patient.class);

        doNothing().when(emailService).sendMessage(any(), any());
        VisitToAddRequest underTestVisit = new VisitToAddRequest(doctor.getId(), patient.getId(),
                LocalDateTime.now().plusYears(1L));
        String contentVisit = objectMapper.writeValueAsString(underTestVisit);
        MvcResult mvcResultVisit = mockMvc.perform(post("/visit/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentVisit)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        VisitIdDto visitIdDto = objectMapper.readValue(mvcResultVisit.getResponse().getContentAsString(), VisitIdDto.class);
        Visit visit = visitService.getVisitById(visitIdDto.getId());
        Token token = visitService.getTokenById(visit.getTokenId());

        String contentToken = token.getCode();
        mockMvc.perform(get("/visit/" + contentToken + "/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contentToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("confirmed"));
    }

    @Test
    void shouldGetVisit() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        VisitToAddRequest underTest = new VisitToAddRequest(1, 1,
                LocalDateTime.now().plusYears(1L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/visit/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void shouldNotGetVisitAndThrowsNotFoundDoctor() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        VisitToAddRequest underTest = new VisitToAddRequest(1500, 1,
                LocalDateTime.now().plusYears(1L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/visit/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("METHOD_ARGUMENT_NOT_VALID_EXCEPTION"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("doctorId"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("ERROR_ENTITY_EXISTS"));
    }

    @Test
    void shouldNotGetVisitAndThrowsNotFoundPatient() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        VisitToAddRequest underTest = new VisitToAddRequest(1, 1500,
                LocalDateTime.now().plusYears(1L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/visit/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("METHOD_ARGUMENT_NOT_VALID_EXCEPTION"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("patientId"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("ERROR_ENTITY_EXISTS"));
    }

    @Test
    void shouldNotGetVisitAndThrowsDoctorIsNotAvailable() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        VisitToAddRequest underTest = new VisitToAddRequest(1, 1500,
                LocalDateTime.now().plusYears(1L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/visit/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("patientId"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("ERROR_ENTITY_EXISTS"));
    }

    @Test
    void shouldNotGetVisitAndThrowsValidDate() throws Exception {
        doNothing().when(emailService).sendMessage(any(), any());
        VisitToAddRequest underTest = new VisitToAddRequest(1, 1,
                LocalDateTime.now().minusYears(1L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/visit/booked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("date"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("must be a future date"));
    }

    @Test
    void shouldConfirmVisit() throws Exception {
        String content = objectMapper.writeValueAsString("token12345");
        mockMvc.perform(get("/visit/token12345/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("confirmed"));
    }

    @Test
    void shouldNotConfirmVisitAndThrowsTokenNotFoundException() throws Exception {
        String content = objectMapper.writeValueAsString("tokenNotFound");
        mockMvc.perform(get("/visit/tokenNotFound/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.entityName").value("Token"))
                .andExpect(jsonPath("$.code").value("TOKEN_NOT_FOUND_EXCEPTION"));
    }

    @Test
    void shouldCancelVisit() throws Exception {
        String content = objectMapper.writeValueAsString("token1");
        mockMvc.perform(get("/visit/token1/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("canceled"));
    }

    @Test
    void shouldNotCancelVisitAndThrowsTokenNotFoundException() throws Exception {
        String content = objectMapper.writeValueAsString("tokenNotFound");
        mockMvc.perform(get("/visit/tokenNotFound/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.entityName").value("Token"))
                .andExpect(jsonPath("$.code").value("TOKEN_NOT_FOUND_EXCEPTION"));
    }

    @Test
    void shouldFindTopNearestVisits() throws Exception {
        FindVisitsRequest underTest = new FindVisitsRequest("kardiolog", "pies"
                , LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(5L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/visit/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldNotFindTopNearestVisitsAndThrowsNotFoundDoctorWithTheGivenParametersOfSpecializationsException() throws Exception {
        FindVisitsRequest underTest = new FindVisitsRequest("Test", "Test"
                , LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/visit/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_WITH_GIVEN_PARAMETERS"))
                .andExpect(jsonPath("$.medicalSpecializationError").value("Test"))
                .andExpect(jsonPath("$.animalSpecializationError").value("Test"));
    }

    @Test
    void shouldNotFindTopNearestVisitsAndThrowsNotFoundFreeVisitAtGivenTimeException() throws Exception {
        FindVisitsRequest underTest = new FindVisitsRequest("neurolog", "kot"
                , LocalDateTime.of(2022, Month.JULY, 15, 16, 15, 00),
                LocalDateTime.of(2022, Month.JULY, 15, 17, 15, 00));
        String content = objectMapper.writeValueAsString(underTest);
        mockMvc.perform(post("/visit/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_FREE_VISIT_AT_GIVEN_TIME_EXCEPTION"))
                .andExpect(jsonPath("$.errorDateFrom").value("2022-07-15T16:15"))
                .andExpect(jsonPath("$.errorDateTo").value("2022-07-15T17:15"));
    }
}