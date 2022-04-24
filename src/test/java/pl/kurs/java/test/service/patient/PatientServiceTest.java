package pl.kurs.java.test.service.patient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kurs.java.test.entity.Patient;
import pl.kurs.java.test.model.CreatePatientRequest;
import pl.kurs.java.test.repository.PatientRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PatientServiceTest {

    private PatientService patientService;
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        patientRepository = mock(PatientRepository.class);
        patientService = new PatientService(patientRepository);
    }

    @Test
    void saveNewPatientTest() {
        CreatePatientRequest model = new CreatePatientRequest("test1", "pies", "pitbull", 2,
                "test1", "test1", "test2022@gmail.com");
        when(patientRepository.saveAndFlush(any(Patient.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Patient given = patientService.saveNewPatient(model);
        Patient expected = new Patient("test1", "pies", "pitbull", 2,
                "test1", "test1", "test2022@gmail.com", true);
        assertEquals(given.getAnimalName(), expected.getAnimalName());
        assertEquals(given.getEmail(),expected.getEmail());
    }

    @Test
    void existsByIdTest() {
        when(patientRepository.existsById(anyInt())).thenReturn(true);
        assertEquals(patientService.existsById(1), true);
    }
}