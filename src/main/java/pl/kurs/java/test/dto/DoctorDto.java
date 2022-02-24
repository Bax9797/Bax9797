package pl.kurs.java.test.dto;

import lombok.Data;

@Data
public class DoctorDto {

    private int id;
    private String name;
    private String surname;
    private String medicalSpecialization;
    private String animalSpecialization;
    private double rate;
    private String nip;
}
