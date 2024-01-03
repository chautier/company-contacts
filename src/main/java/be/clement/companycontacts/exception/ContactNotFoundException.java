package be.clement.companycontacts.exception;

import static java.lang.String.format;

public class ContactNotFoundException extends RuntimeException {

    public ContactNotFoundException(Long id) {
        super(
                format("Contact with id [%s] not found", id)
        );
    }

}
