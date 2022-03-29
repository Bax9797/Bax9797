package pl.kurs.java.test.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medical_staff")
@Accessors(chain = true)
@Where(clause = "hired = true")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String medicalSpecialization;
    private String animalSpecialization;
    private double rate;
    @Column(unique = true)
    private String nip;
    private boolean hired;
    @OneToMany(mappedBy = "doctor",
            cascade = CascadeType.ALL)
    private Set<Visit> visits;
    @Version
    private int version;

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
