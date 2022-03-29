package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.java.test.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    boolean existsByEmail(String email);


    @Modifying
    @Transactional
    @Query("UPDATE Patient p SET p.currentCustomer = false, p.version = p.version + 1  WHERE p.id=?1")
    void updateStatusOfPatient(int id);
}
