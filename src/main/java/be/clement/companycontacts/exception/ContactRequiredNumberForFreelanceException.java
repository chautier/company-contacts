package be.clement.companycontacts.exception;

public class ContactRequiredNumberForFreelanceException extends RuntimeException {

    public ContactRequiredNumberForFreelanceException() {
        super("VAT Number is required for freelance");
    }

}
