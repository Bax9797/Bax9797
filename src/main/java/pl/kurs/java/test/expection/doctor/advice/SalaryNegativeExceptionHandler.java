package pl.kurs.java.test.expection.doctor.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.expection.Errors.Error;
import pl.kurs.java.test.expection.doctor.SalaryNegativeException;

@ControllerAdvice
public class SalaryNegativeExceptionHandler {

    @ExceptionHandler(value = SalaryNegativeException.class)
    public ResponseEntity<Object> handlerSalaryNegativeException(SalaryNegativeException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        Error error = new Error()
                .setHttpStatus(badRequest)
                .setStatus(badRequest.value())
                .setMessage(exception.getMessage());
        return new ResponseEntity<>(error, badRequest);
    }
}
