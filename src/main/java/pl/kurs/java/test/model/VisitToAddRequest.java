package pl.kurs.java.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pl.kurs.java.test.annotation.EntityExists;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.entity.Patient;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VisitToAddRequest {

    @EntityExists(type = Doctor.class)
    private int doctorId;
    @EntityExists(type = Patient.class)
    private int patientId;
    @Future
    private LocalDateTime date;
}
