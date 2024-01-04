package be.clement.companycontacts.exception;

import static java.lang.String.format;

public class CompanyNumberNotFoundException extends RuntimeException {

    public CompanyNumberNotFoundException(String number) {
        super(
                format("Company with number [%s] not found", number)
        );
    }

}
