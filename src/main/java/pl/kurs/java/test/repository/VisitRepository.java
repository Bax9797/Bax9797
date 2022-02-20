package pl.kurs.java.test.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.kurs.java.test.entity.Status;
import pl.kurs.java.test.entity.Visit;
import pl.kurs.java.test.model.NearestVisit;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

    Visit getByTokenId(int tokenId);

    boolean existsByTokenId(int tokenId);

    @Modifying
    @Transactional
    @Query("SELECT meetingDate from Visit where doctor_id=?1")
    List<LocalDateTime> getDateByDoctorId(int doctorId);

    @Transactional
    @Query("SELECT meetingDate from Visit where patient_id=?1")
    List<LocalDateTime> getDateByPatientId(int patientId);

    @Modifying
    @Transactional
    @Query("UPDATE Visit v SET v.status = ?1 WHERE v.id = ?2")
    void updateVisitStatus(Status status, int id);
}
