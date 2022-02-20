package pl.kurs.java.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelDoctorToAdd {

    private String name;
    private String surname;
    private String medicalSpecialization;
    private String animalSpecialization;
    private double rate;
    private String nip;
}
