package pl.kurs.java.test.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient_list")
@Accessors(chain = true)
@Where(clause = "current_customer = true")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String animalName;
    private String animalSpecies;
    private String animalBreed;
    private int age;
    private String ownerName;
    private String ownerSurname;
    @Column(unique = true)
    private String email;
    private Boolean currentCustomer;
    @OneToMany(mappedBy = "patient",
            cascade = CascadeType.ALL)
    private Set<Visit> visits;
    @Version
    private int version;

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
