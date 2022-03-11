package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kurs.java.test.entity.Doctor;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Doctor d SET d.hired = ?1 WHERE d.id = ?2")
    void updateDoctorHired(boolean hired, int id);

    List<Doctor> findAllByMedicalSpecializationAndAnimalSpecializationAndHired(String medicalSpecialization, String animalSpecialization, boolean hired);

    boolean existsByNip(String nip);

    @Transactional
    @Query("SELECT hired FROM Doctor WHERE id = ?1")
    boolean getStatusHiredById(int id);
}
