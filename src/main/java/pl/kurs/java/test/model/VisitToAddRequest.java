package pl.kurs.java.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pl.kurs.java.test.annotation.DoctorIdNotExisting;
import pl.kurs.java.test.annotation.PatientIdNotExisting;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VisitToAddRequest {

    @DoctorIdNotExisting
    private int doctorId;
    @PatientIdNotExisting
    private int patientId;
    @Future
    private LocalDateTime date;
}
