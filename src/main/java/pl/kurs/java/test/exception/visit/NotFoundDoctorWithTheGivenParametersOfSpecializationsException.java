package pl.kurs.java.test.exception.visit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundDoctorWithTheGivenParametersOfSpecializationsException extends RuntimeException {
    private String medicalSpecialization;
    private String animalSpecialization;

    public String getMedicalSpecialization() {
        return medicalSpecialization;
    }

    public String getAnimalSpecialization() {
        return animalSpecialization;
    }

    public NotFoundDoctorWithTheGivenParametersOfSpecializationsException(String medicalSpecialization, String animalSpecialization) {
        super();
        this.medicalSpecialization = medicalSpecialization;
        this.animalSpecialization = animalSpecialization;
    }
}
