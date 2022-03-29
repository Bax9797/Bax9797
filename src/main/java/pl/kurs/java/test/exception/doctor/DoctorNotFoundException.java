package pl.kurs.java.test.exception.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(int id) {
        super("Doctor not found with id : " + id);
    }
}
