package pl.kurs.java.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kurs.java.test.annotation.NipNotExisting;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorRequest {
    @NotBlank(message = "field name must be not empty")
    @NotNull(message = "field name must be not null")
    private String name;
    @NotBlank(message = "field surname must be not empty")
    @NotNull(message = "field surname must be not null")
    private String surname;
    @NotBlank(message = "field medicalSpecialization must be not empty")
    @NotNull(message = "field medicalSpecialization must be not null")
    private String medicalSpecialization;
    @NotBlank(message = "field animalSpecialization must be not empty")
    @NotNull(message = "field animalSpecialization must be not null")
    private String animalSpecialization;
    @Min(value = 1, message = "rate cannot be negative")
    private double rate;
    @NotBlank(message = "field nip must be not empty")
    @NotNull(message = "field nip must be not null")
    @NipNotExisting
    private String nip;
}
