package be.clement.companycontacts.controller;

import be.clement.companycontacts.exception.CompanyAlreadyExistsException;
import be.clement.companycontacts.exception.CompanyNotFoundException;
import be.clement.companycontacts.exception.CompanyNumberNotFoundException;
import be.clement.companycontacts.exception.ContactAlreadyAddedException;
import be.clement.companycontacts.exception.ContactNotFoundException;
import be.clement.companycontacts.exception.ContactRequiredNumberForFreelanceException;
import be.clement.model.Error;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({
            CompanyNotFoundException.class,
            CompanyNumberNotFoundException.class,
            ContactNotFoundException.class
    })
    public ResponseEntity<Error> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .type("[NOT_FOUND]"),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            ContactAlreadyAddedException.class,
            CompanyAlreadyExistsException.class,
            ContactRequiredNumberForFreelanceException.class
    })
    public ResponseEntity<Error> handleBadRequestException(Exception ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .type("[BAD_REQUEST]"),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getBindingResult()
                                .getFieldErrors().stream()
                                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage))
                                .toString())
                        .type("[BAD_REQUEST]"),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

}
