package pl.kurs.java.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.exception.errors.ErrorsResponse;
import pl.kurs.java.test.exception.errors.FieldValidationError;

@ControllerAdvice
public class HandlerMethodArgumentNotValid {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        ErrorsResponse errorResult = new ErrorsResponse();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorResult.getFieldErrors()
                    .add(new FieldValidationError(fieldError.getField(),
                            fieldError.getDefaultMessage()));
        }
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
