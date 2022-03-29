package pl.kurs.java.test.service.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.entity.Patient;
import pl.kurs.java.test.exception.patient.PatientNotFoundException;
import pl.kurs.java.test.model.CreatePatientRequest;
import pl.kurs.java.test.repository.PatientRepository;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;

    public Patient findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    public Patient saveNewPatient(CreatePatientRequest createPatientRequest) {
        Patient patient = new Patient()
                .setAnimalName(createPatientRequest.getAnimalName())
                .setAnimalSpecies(createPatientRequest.getAnimalSpecies())
                .setAnimalBreed(createPatientRequest.getAnimalBreed())
                .setAge(createPatientRequest.getAge())
                .setOwnerName(createPatientRequest.getOwnerName())
                .setOwnerSurname(createPatientRequest.getOwnerSurname())
                .setEmail(createPatientRequest.getEmail())
                .setCurrentCustomer(true);
        repository.saveAndFlush(patient);
        return patient;
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public void removePatient(int id) {
        Patient patient = findById(id);
        repository.updateStatusOfPatient(patient.getId());
    }

    public Page<Patient> findAllToPage(Pageable page) {
        return repository.findAll(page);
    }
}