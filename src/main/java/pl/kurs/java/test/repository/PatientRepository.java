package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.kurs.java.test.entity.Patient;

import javax.transaction.Transactional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Patient p SET p.currentCustomer = ?1 WHERE p.id = ?2")
    void updatePatientCurrentCustomer(boolean currentCustomer, int id);
}
