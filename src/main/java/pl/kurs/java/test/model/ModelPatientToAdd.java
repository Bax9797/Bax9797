package pl.kurs.java.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelPatientToAdd {

    private String animalName;
    private String animalSpecies;
    private String animalBreed;
    private int age;
    private String ownerName;
    private String ownerSurname;
    private  String email;
}
