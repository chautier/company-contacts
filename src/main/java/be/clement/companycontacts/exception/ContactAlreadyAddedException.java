package be.clement.companycontacts.exception;

import static java.lang.String.format;

public class ContactAlreadyAddedException extends RuntimeException {

    public ContactAlreadyAddedException(Long contactId, Long companyId) {
        super(
                format("Contact with id [%s] already added in company [%s]", contactId, companyId)
        );
    }

}
