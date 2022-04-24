package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.java.test.entity.Doctor;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Doctor> findAllByMedicalSpecializationAndAnimalSpecialization(String medicalSpecialization, String animalSpecialization);

    boolean existsByNip(String nip);

    @Transactional
    @Modifying
    @Query("UPDATE Doctor d SET d.hired = false, d.version = d.version + 1  WHERE d.id=?1")
    void updateStatusOfDoctorHired(int id);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Doctor d WHERE d.id = ?1")
    Optional<Doctor> findByIdForWrite(int id);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT d FROM Doctor d WHERE d.id = ?1")
    Optional<Doctor> findByIdForRead(int id);
}
