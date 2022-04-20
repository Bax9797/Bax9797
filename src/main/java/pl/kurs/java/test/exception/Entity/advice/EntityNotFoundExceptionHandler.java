package pl.kurs.java.test.exception.Entity.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.exception.Entity.EntityNotFoundException;
import pl.kurs.java.test.exception.Entity.ErrorEntityExists;

@ControllerAdvice
public class EntityNotFoundExceptionHandler {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(EntityNotFoundException exception) {
        ErrorEntityExists response = new ErrorEntityExists()
                .setErrorCode("ENTITY_NOT_FOUND")
                .setEntityName(exception.getName())
                .setId(exception.getId());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
