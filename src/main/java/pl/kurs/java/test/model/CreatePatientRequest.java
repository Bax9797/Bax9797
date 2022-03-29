package pl.kurs.java.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kurs.java.test.annotation.EmailNotExisting;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientRequest {

    @NotBlank(message = "field animal name must be not empty")
    @NotNull(message = "field animal name must be not null")
    private String animalName;
    @NotBlank(message = "field animal species must be not empty")
    @NotNull(message = "field animal species must be not null")
    private String animalSpecies;
    @NotBlank(message = "field animal breed  must be not empty")
    @NotNull(message = "field animal breed must be not null")
    private String animalBreed;
    @Min(value = 0, message = "animal age cannot be negative")
    private int age;
    @NotBlank(message = "field owner name must be not empty")
    @NotNull(message = "field owner name must be not null")
    private String ownerName;
    @NotBlank(message = "field owner surname must be not empty")
    @NotNull(message = "field owner surname must be not null")
    private String ownerSurname;
    @EmailNotExisting
    @NotBlank(message = "field email must be not empty")
    private String email;
}
