package pl.kurs.java.test.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.exception.errors.ErrorMessageResponse;
import pl.kurs.java.test.exception.errors.ErrorsResponse;
import pl.kurs.java.test.exception.errors.FieldValidationError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        ErrorsResponse errorResult = new ErrorsResponse().setCode("METHOD_ARGUMENT_NOT_VALID_EXCEPTION");
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorResult.getFieldErrors()
                    .add(new FieldValidationError(fieldError.getField(),
                            fieldError.getDefaultMessage()));
        }
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlExceptionHandler(SQLException exception) {
        ErrorMessageResponse response = new ErrorMessageResponse()
                .setCode("SQL_EXCEPTION")
                .setEntityName(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintExceptionHandler(ConstraintViolationException exception) {
        ErrorsResponse errorResult = new ErrorsResponse().setCode("CONSTRAINT_EXCEPTION");
        for (ConstraintViolation error : exception.getConstraintViolations()) {
            errorResult.getFieldErrors()
                    .add(new FieldValidationError(error.getPropertyPath().toString(),
                            error.getMessage()));
        }
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}