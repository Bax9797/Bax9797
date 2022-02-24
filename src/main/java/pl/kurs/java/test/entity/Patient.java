package pl.kurs.java.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient_list")
@Accessors(chain = true)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "animal_name")
    private String animalName;
    @Column(name = "animal_species")
    private String animalSpecies;
    @Column(name = "animal_breed")
    private String animalBreed;
    @Column(name = "age")
    private int age;
    @Column(name = "owner_name")
    private String ownerName;
    @Column(name = "owner_surname")
    private String ownerSurname;
    @NotEmpty
    @Email
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "current_customer")
    private Boolean currentCustomer;
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Visit> visits;

    public Patient(String animalName, String animalSpecies, String animalBreed, int age, String ownerName, String ownerSurname, String email, Boolean currentCustomer) {
        this.animalName = animalName;
        this.animalSpecies = animalSpecies;
        this.animalBreed = animalBreed;
        this.age = age;
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.email = email;
        this.currentCustomer = currentCustomer;
    }

}
