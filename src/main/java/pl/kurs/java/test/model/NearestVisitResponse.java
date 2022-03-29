package pl.kurs.java.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import pl.kurs.java.test.dto.DoctorVisitDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class NearestVisitResponse {

    private DoctorVisitDto doctor;
    private LocalDateTime dateTime;
}
