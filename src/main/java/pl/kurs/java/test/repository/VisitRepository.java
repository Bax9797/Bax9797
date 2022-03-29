package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.java.test.entity.Status;
import pl.kurs.java.test.entity.Visit;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Visit v SET v.status = ?1 WHERE v.id = ?2")
    void updateVisitStatus(Status status, int id);

    List<Visit> findByDoctorId(int doctorId);

    List<Visit> findByPatientId(int patientId);

    Optional<Visit> findByTokenId(int id);
}
