package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.java.test.entity.Doctor;

import javax.transaction.Transactional;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findAllByMedicalSpecializationAndAnimalSpecialization(String medicalSpecialization, String animalSpecialization);

    boolean existsByNip(String nip);

    @Modifying
    @Transactional
    @Query("UPDATE Doctor d SET d.hired = false, d.version = d.version + 1  WHERE d.id=?1")
    void updateStatusOfDoctorHired(int id);
}
