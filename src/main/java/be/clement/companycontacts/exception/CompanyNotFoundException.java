package be.clement.companycontacts.exception;

import static java.lang.String.format;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(Long id) {
        super(
                format("Company with id [%s] not found", id)
        );
    }

}
