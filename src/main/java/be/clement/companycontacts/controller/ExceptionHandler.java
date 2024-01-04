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
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({ContactNotFoundException.class})
    public ResponseEntity<Error> handleContactNotFoundException(Exception ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .type("[NOT_FOUND]"),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ContactRequiredNumberForFreelanceException.class})
    public ResponseEntity<Error> handleContactRequiredNumberForFreelanceException(Exception ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .type("[BAD_REQUEST]"),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({CompanyNumberNotFoundException.class})
    public ResponseEntity<Error> handleCompanyNumberNotFoundException(Exception ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .type("[NOT_FOUND]"),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({CompanyAlreadyExistsException.class})
    public ResponseEntity<Error> handleCompanyAlreadyExistsException(Exception ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .type("[BAD_REQUEST]"),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({CompanyNotFoundException.class})
    public ResponseEntity<Error> handleCompanyNotFoundException(Exception ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .type("[NOT_FOUND]"),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ContactAlreadyAddedException.class})
    public ResponseEntity<Error> handleContactAlreadyAddedException(Exception ex) {
        return new ResponseEntity<>(
                new Error()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage())
                        .type("[BAD_REQUEST]"),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

}
