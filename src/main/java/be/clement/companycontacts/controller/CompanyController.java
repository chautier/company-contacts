package be.clement.companycontacts.controller;

import be.clement.api.CompaniesApi;
import be.clement.companycontacts.service.CompanyService;
import be.clement.model.Company;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompanyController implements CompaniesApi {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Void> contactToCompany(Integer companyId, Integer contactId) {
        service.contactToCompany(Long.valueOf(companyId), Long.valueOf(contactId));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Company> createCompany(Company company) {
        return new ResponseEntity<>(
                service.createCompany(company),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<Company> getCompanyByNumber(String number) {
        return new ResponseEntity<>(
                service.getCompanyByNumber(number),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<List<Company>> listCompanies() {
        return new ResponseEntity<>(
                service.listCompanies(),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Company> updateCompany(Integer id, Company company) {
        return new ResponseEntity<>(
                service.updateCompany(Long.valueOf(id), company),
                HttpStatus.OK
        );
    }
}
