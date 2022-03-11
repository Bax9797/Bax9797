package pl.kurs.java.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.java.test.exception.Errors.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class HandlerMethodArgumentNotValid {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerSalaryNegativeException(MethodArgumentNotValidException exception) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
