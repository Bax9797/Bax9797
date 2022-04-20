package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.entity.Patient;

import javax.persistence.LockModeType;
import java.util.Optional;


public interface PatientRepository extends JpaRepository<Patient, Integer> {
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE Patient p SET p.currentCustomer = false, p.version = p.version + 1  WHERE p.id=?1")
    void updateStatusOfPatient(int id);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Patient p WHERE .id = ?1")
    Optional<Patient> findByIdForWrite(int id);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT p FROM Patient p WHERE .id = ?1")
    Optional<Patient> findByIdForRead(int id);
}
