package pl.kurs.java.test.service.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.expection.doctor.DoctorNotFoundException;
import pl.kurs.java.test.expection.doctor.DuplicateNipException;
import pl.kurs.java.test.expection.doctor.EmptyFieldsException;
import pl.kurs.java.test.expection.doctor.SalaryNegativeException;
import pl.kurs.java.test.model.ModelDoctorToAdd;
import pl.kurs.java.test.repository.DoctorRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository repository;

    public Doctor findById(int id) {
        Optional<Doctor> obj = repository.findById(id);
        return obj.orElseThrow(() -> new DoctorNotFoundException("User Not Found with id : " + id));
    }

    public Doctor saveNewDoctor(ModelDoctorToAdd modelDoctorToAdd) {
        Doctor doctor = new Doctor()
                .setName(modelDoctorToAdd.getName())
                .setSurname(modelDoctorToAdd.getSurname())
                .setMedicalSpecialization(modelDoctorToAdd.getMedicalSpecialization())
                .setAnimalSpecialization(modelDoctorToAdd.getAnimalSpecialization())
                .setRate(modelDoctorToAdd.getRate())
                .setNip(modelDoctorToAdd.getNip())
                .setHired(true);
        repository.saveAndFlush(doctor);
        return doctor;
    }

    public Doctor validationOfTheEnteredParameterData(ModelDoctorToAdd modelDoctorToAdd) {
        if (modelDoctorToAdd.getRate() < 0) {
            throw new SalaryNegativeException("salary cannot be negative");
        } else if (!checkIfNipAlreadyExists(modelDoctorToAdd) == true) {
            throw new DuplicateNipException("duplicate tax identification number. There is a person with the given tax identification number in the database");
        } else if (isBlank(modelDoctorToAdd) == true) {
            throw new EmptyFieldsException("all fields must be not empty or the rate was entered incorrectly");
        } else {
            return saveNewDoctor(modelDoctorToAdd);
        }
    }

    private Boolean isBlank(ModelDoctorToAdd modelDoctorToAdd) {
        if (modelDoctorToAdd.getName().isBlank() || modelDoctorToAdd.getSurname().isBlank() ||
                modelDoctorToAdd.getAnimalSpecialization().isBlank() || modelDoctorToAdd.getMedicalSpecialization().isBlank() ||
                modelDoctorToAdd.getNip().isBlank() || modelDoctorToAdd.getRate() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfNipAlreadyExists(ModelDoctorToAdd modelDoctorToAdd) {
        long check = repository.findAll().stream().filter(d -> d.getNip().equals(modelDoctorToAdd.getNip())).count();
        if (check != 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public String dismiss(int id) {
        repository.updateDoctorHired(false, id);
        return "changed status of given doctor, this doctor will not be able to handle any visits";
    }
}
