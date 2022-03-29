package pl.kurs.java.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindVisitsRequest {

    private String medicalSpecialization;
    private String animalSpecialization;
    @FutureOrPresent
    private LocalDateTime dateFrom;
    @Future
    private LocalDateTime dateTo;
}
