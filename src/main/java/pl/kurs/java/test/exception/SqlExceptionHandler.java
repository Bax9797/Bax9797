package pl.kurs.java.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.exception.errors.ErrorMessageResponse;

import java.sql.SQLException;

@ControllerAdvice
public class SqlExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> SqlExceptionHandler(SQLException exception) {
        ErrorMessageResponse response = new ErrorMessageResponse().setErrorMessage(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
