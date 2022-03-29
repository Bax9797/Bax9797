package pl.kurs.java.test.dto;

import lombok.Data;

@Data
public class PatientDto {
    private int id;
    private String animalName;
    private String animalSpecies;
    private String animalBreed;
    private int age;
    private String ownerName;
    private String ownerSurname;
    private String email;
}
