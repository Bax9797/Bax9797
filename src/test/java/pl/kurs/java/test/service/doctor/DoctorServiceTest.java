package pl.kurs.java.test.service.doctor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.model.ModelDoctorToAdd;
import pl.kurs.java.test.repository.DoctorRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DoctorServiceTest {

    private DoctorRepository doctorRepository;
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        doctorRepository = mock(DoctorRepository.class);
        doctorService = new DoctorService(doctorRepository);
    }

    @Test
    void findByIdTest() {
        Doctor expected = new Doctor(1, "Test1", "Test1",
                "Kardiolog", "Psy", 100.00, "Test1", true);
        when(doctorRepository.findById(any())).thenReturn(Optional.of(expected));
        Doctor given = doctorService.findById(1);
        assertEquals(given, expected);
    }

    @Test
    void saveNewDoctorTest() {
        ModelDoctorToAdd modelDoctorToAdd = new ModelDoctorToAdd("Test1", "Test1",
                "Kardiolog", "Psy", 100.00, "Test1");
        when(doctorRepository.saveAndFlush(any(Doctor.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Doctor given = doctorService.saveNewDoctor(modelDoctorToAdd);
        Doctor expected = new Doctor().setName("Test1").setSurname("Test1").setMedicalSpecialization("Kardiolog")
                .setAnimalSpecialization("Psy").setRate(100.00).setNip("Test1").setHired(true);
        assertEquals(given, expected);
    }

    @Test
    void validationOfTheEnteredParameterDataTest() {
        ModelDoctorToAdd modelDoctorToAdd = new ModelDoctorToAdd("Test1", "Test1",
                "Kardiolog", "Psy", 100.00, "Test1");
        when(doctorRepository.saveAndFlush(any(Doctor.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        Doctor given = doctorService.validationOfTheEnteredParameterData(modelDoctorToAdd);
        Doctor expected = new Doctor().setName("Test1").setSurname("Test1").setMedicalSpecialization("Kardiolog")
                .setAnimalSpecialization("Psy").setRate(100.00).setNip("Test1").setHired(true);
        assertEquals(given, expected);
    }

    @Test
    void existsByIdTest() {
        when(doctorRepository.existsById(any())).thenReturn(true);
        assertEquals(doctorService.existsById(1), true);
    }
}