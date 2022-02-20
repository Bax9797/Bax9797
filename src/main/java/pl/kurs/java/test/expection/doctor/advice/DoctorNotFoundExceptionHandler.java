package pl.kurs.java.test.expection.doctor.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.expection.Errors.Error;
import pl.kurs.java.test.expection.doctor.DoctorNotFoundException;

@ControllerAdvice
public class DoctorNotFoundExceptionHandler {

    @ExceptionHandler(value = DoctorNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(DoctorNotFoundException exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
