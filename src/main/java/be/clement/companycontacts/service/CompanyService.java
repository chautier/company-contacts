package be.clement.companycontacts.service;

import be.clement.companycontacts.dto.CompanyDto;
import be.clement.companycontacts.dto.ContactDto;
import be.clement.companycontacts.dto.mapper.CompanyMapper;
import be.clement.companycontacts.exception.CompanyAlreadyExistsException;
import be.clement.companycontacts.exception.CompanyNotFoundException;
import be.clement.companycontacts.exception.CompanyNumberNotFoundException;
import be.clement.companycontacts.exception.ContactAlreadyAddedException;
import be.clement.companycontacts.repository.CompanyRepository;
import be.clement.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository repository;
    private final ContactService contactService;

    public CompanyService(CompanyRepository repository,
                          ContactService contactService) {
        this.repository = repository;
        this.contactService = contactService;
    }

    public void contactToCompany(Long companyId, Long contactId) {
        //  Check if company exists
        CompanyDto companyDto = repository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        //  Check if contact exists and if he is already added
        ContactDto contact = contactService.find(contactId);
        if (companyDto.getContacts().stream()
                .anyMatch(c -> c.getId().equals(contactId))) {
            throw new ContactAlreadyAddedException(contactId, companyId);
        }

        companyDto.getContacts().add(contact);

        repository.saveAndFlush(
                companyDto
        );
    }

    public Company createCompany(Company company) {
        //  Check if number already exists
        repository.findByNumber(company.getNumber())
                .ifPresent(c -> {
                    throw new CompanyAlreadyExistsException(company.getNumber());
                });

        return CompanyMapper.INSTANCE.mapToCompany(
                repository.saveAndFlush(
                        CompanyMapper.INSTANCE.mapToCompanyDto(company)
                )
        );
    }

    public Company getCompanyByNumber(String number) {
        return repository.findByNumber(number)
                .map(CompanyMapper.INSTANCE::mapToCompany)
                .orElseThrow(() -> new CompanyNumberNotFoundException(number));
    }

    public List<Company> listCompanies() {
        return CompanyMapper.INSTANCE.mapToListOfCompany(
                repository.findAll()
        );
    }

    public Company updateCompany(Long id, Company company) {
        //  Check if company exists
        Optional<CompanyDto> companyDto = repository.findById(id);
        if (companyDto.isEmpty()) {
            throw new CompanyNotFoundException(id);
        }

        //  Checks if the potential new number doesn't exist
        repository.findByNumber(company.getNumber())
                .filter(dto -> !dto.getId().equals(id))
                .ifPresent(dto -> {
                    throw new CompanyAlreadyExistsException(company.getNumber());
                });

        CompanyDto newCompany = CompanyMapper.INSTANCE.mapToCompanyDto(company);
        newCompany.setId(id);
        newCompany.setContacts(companyDto.orElseThrow().getContacts());

        return CompanyMapper.INSTANCE.mapToCompany(
                repository.saveAndFlush(
                        newCompany
                )
        );
    }
}
