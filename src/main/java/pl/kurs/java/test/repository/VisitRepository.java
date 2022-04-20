package pl.kurs.java.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.java.test.entity.Status;
import pl.kurs.java.test.entity.Visit;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Visit v SET v.status = ?1, v.version = v.version + 1 WHERE v.id = ?2")
    void updateVisitStatus(Status status, int id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Visit> findByDoctorId(int doctorId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Visit> findByPatientId(int patientId);

    Optional<Visit> findByTokenId(int id);
}
