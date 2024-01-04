package be.clement.companycontacts.exception;

import static java.lang.String.format;

public class CompanyAlreadyExistsException extends RuntimeException {

    public CompanyAlreadyExistsException(String number) {
        super(
                format("Company with number [%s] already exists", number)
        );
    }

}
