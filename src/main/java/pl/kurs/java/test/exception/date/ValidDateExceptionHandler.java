package pl.kurs.java.test.exception.date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kurs.java.test.exception.errors.ErrorMessageResponse;

@RestControllerAdvice
public class ValidDateExceptionHandler {

    @ExceptionHandler(value = ValidDateException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(ValidDateException exception) {
        ErrorMessageResponse response = new ErrorMessageResponse()
                .setCode("ENTITY_ALREADY_ON_VISIT")
                .setEntityName(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
