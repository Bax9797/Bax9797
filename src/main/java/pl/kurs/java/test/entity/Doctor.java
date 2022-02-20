package pl.kurs.java.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medical_staff")
@Accessors(chain = true)
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    @Column(name = "medical_specialization")
    private String medicalSpecialization;
    @Column(name = "animal_specialization")
    private String animalSpecialization;
    private double rate;
    private String nip;
    private boolean hired;

    public Doctor(String name, String surname, String medicalSpecialization, String animalSpecialization, double rate, String nip, boolean hired) {
        this.name = name;
        this.surname = surname;
        this.medicalSpecialization = medicalSpecialization;
        this.animalSpecialization = animalSpecialization;
        this.rate = rate;
        this.nip = nip;
        this.hired = hired;
    }
}
