package pl.kurs.java.test.service.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.entity.Patient;
import pl.kurs.java.test.expection.doctor.EmptyFieldsException;
import pl.kurs.java.test.expection.patient.AgeAnimalNegativeException;
import pl.kurs.java.test.expection.patient.DuplicateEmailException;
import pl.kurs.java.test.expection.patient.PatientNotFoundException;
import pl.kurs.java.test.model.ModelPatientToAdd;
import pl.kurs.java.test.repository.PatientRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;

    public Patient findById(int id) {
        Optional<Patient> obj = repository.findById(id);
        return obj.orElseThrow(() -> new PatientNotFoundException("User Not Found with id : " + id));
    }

    public Patient saveNewPatient(ModelPatientToAdd modelPatientToAdd) {
        Patient patient = new Patient()
                .setAnimalName(modelPatientToAdd.getAnimalName())
                .setAnimalSpecies(modelPatientToAdd.getAnimalSpecies())
                .setAnimalBreed(modelPatientToAdd.getAnimalBreed())
                .setAge(modelPatientToAdd.getAge())
                .setOwnerName(modelPatientToAdd.getOwnerName())
                .setOwnerSurname(modelPatientToAdd.getOwnerSurname())
                .setEmail(modelPatientToAdd.getEmail())
                .setCurrentCustomer(true);
        repository.saveAndFlush(patient);
        return patient;
    }

    public Patient validationOfTheEnteredParameterData(ModelPatientToAdd modelPatientToAdd) {
        if (modelPatientToAdd.getAge() < 0) {
            throw new AgeAnimalNegativeException("animal age can not be less than 0");
        } else if (!checkIfEmailAlreadyExists(modelPatientToAdd) == true) {
            throw new DuplicateEmailException("Email has to be unique. There is a person with the given email in the database");
        } else if (isBlank(modelPatientToAdd) == true) {
            throw new EmptyFieldsException("all fields must be not empty");
        } else {
            return saveNewPatient(modelPatientToAdd);
        }
    }

    private boolean isBlank(ModelPatientToAdd modelPatientToAdd) {
        if (modelPatientToAdd.getAnimalName().isBlank() || modelPatientToAdd.getAnimalSpecies().isBlank() ||
                modelPatientToAdd.getAnimalBreed().isBlank() || modelPatientToAdd.getOwnerName().isBlank() ||
                modelPatientToAdd.getOwnerSurname().isBlank() || modelPatientToAdd.getEmail().isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfEmailAlreadyExists(ModelPatientToAdd modelPatientToAdd) {
        long check = repository.findAll().stream().filter(d -> d.getEmail().equals(modelPatientToAdd.getEmail())).count();
        if (check != 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public String removePatient(int id) {
        repository.updatePatientCurrentCustomer(false, id);
        return "changed status of given patient, this client is no longer our patient";
    }
}