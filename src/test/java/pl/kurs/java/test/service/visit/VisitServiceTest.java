package pl.kurs.java.test.service.visit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kurs.java.test.entity.*;
import pl.kurs.java.test.mail.EmailService;
import pl.kurs.java.test.repository.DoctorRepository;
import pl.kurs.java.test.repository.PatientRepository;
import pl.kurs.java.test.repository.TokenRepository;
import pl.kurs.java.test.repository.VisitRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VisitServiceTest {

    private VisitService visitService;
    private VisitRepository visitRepository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private TokenRepository tokenGeneratorRepository;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        visitRepository = mock(VisitRepository.class);
        patientRepository = mock(PatientRepository.class);
        doctorRepository = mock(DoctorRepository.class);
        tokenGeneratorRepository = mock(TokenRepository.class);
        emailService = mock(EmailService.class);
        visitService = new VisitService(
                visitRepository, patientRepository, doctorRepository, tokenGeneratorRepository, emailService);
    }

    @Test
    void getTokenById() {
        Token underTest = new Token()
                .setCode("test").setId(1)
                .setExpireDate(LocalDateTime.now()
                        .plusHours(1l));
        when(tokenGeneratorRepository.findById(anyInt())).thenReturn(Optional.of(underTest));
        Token expected = visitService.getTokenById(1);
        assertEquals(underTest, expected);

    }

    @Test
    void getVisitById() {
        Doctor doctorUnderTest = new Doctor("Test1", "Test1",
                "Kardiolog", "Psy", 100.00, "Test1", true);
        Patient patientUnderTest = new Patient("test1", "test1", "test1", 1, "test1",
                "test1", "test2022@gmail.com", true);
        Visit excepted = new Visit()
                .setDoctor(doctorUnderTest)
                .setPatient(patientUnderTest)
                .setStartVisit(LocalDateTime.now())
                .setEndVisit(LocalDateTime.now().plusHours(1L))
                .setTokenId(1)
                .setStatus(Status.BOOKED);
        when(visitRepository.findById(anyInt())).thenReturn(Optional.of(excepted));
        Visit given = visitService.getVisitById(anyInt());
        assertEquals(given, excepted);
    }
}