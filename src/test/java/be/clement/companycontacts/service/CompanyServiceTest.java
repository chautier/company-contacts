package be.clement.companycontacts.service;

import be.clement.companycontacts.dto.AddressDto;
import be.clement.companycontacts.dto.CompanyDto;
import be.clement.companycontacts.dto.ContactDto;
import be.clement.companycontacts.dto.mapper.CompanyMapper;
import be.clement.companycontacts.exception.CompanyAlreadyExistsException;
import be.clement.companycontacts.exception.CompanyNotFoundException;
import be.clement.companycontacts.exception.CompanyNumberNotFoundException;
import be.clement.companycontacts.exception.ContactAlreadyAddedException;
import be.clement.companycontacts.exception.ContactNotFoundException;
import be.clement.companycontacts.repository.CompanyRepository;
import be.clement.model.Address;
import be.clement.model.Company;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository repository;

    @InjectMocks
    private CompanyService service;

    @Mock
    private ContactService contactService;

    @Test
    void contactToCompany() {
        //  GIVEN
        ArgumentCaptor<CompanyDto> companyDtoArgumentCaptor = ArgumentCaptor.forClass(CompanyDto.class);

        Long companyId = 1L;
        Long contactId = 2L;

        ContactDto contactDto = new ContactDto();
        contactDto.setId(5L);

        CompanyDto companyDto = new CompanyDto();

        //  WHEN
        when(repository.findById(companyId)).thenReturn(Optional.of(companyDto));
        when(contactService.find(contactId)).thenReturn(contactDto);

        //  THEN
        service.contactToCompany(companyId, contactId);

        //  ASSERT
        verify(repository, times(1)).saveAndFlush(companyDtoArgumentCaptor.capture());
        assertThat(companyDtoArgumentCaptor.getValue().getContacts()).contains(contactDto);
    }

    @Test
    void contactToCompany_notFound() {
        //  GIVEN
        Long companyId = 1L;
        Long contactId = 2L;

        //  WHEN
        when(repository.findById(companyId)).thenReturn(Optional.empty());

        //  THEN / ASSERT
        CompanyNotFoundException e = assertThrows(CompanyNotFoundException.class, () -> service.contactToCompany(companyId, contactId));
        assertThat(e.getMessage()).isEqualTo(format("Company with id [%s] not found", companyId));
    }

    @Test
    void contactToCompany_contactNotFound() {
        //  GIVEN
        Long companyId = 1L;
        Long contactId = 2L;

        //  WHEN
        when(repository.findById(companyId)).thenReturn(Optional.of(mock(CompanyDto.class)));
        when(contactService.find(contactId)).thenThrow(ContactNotFoundException.class);

        //  THEN / ASSERT
        assertThrows(ContactNotFoundException.class, () -> service.contactToCompany(companyId, contactId));
    }

    @Test
    void contactToCompany_contactAlreadyAdded() {
        //  GIVEN
        Long companyId = 1L;
        Long contactId = 2L;

        ContactDto contactDto = new ContactDto();
        contactDto.setId(contactId);

        Set<ContactDto> contacts = new HashSet<>(List.of(contactDto));

        CompanyDto companyDto = new CompanyDto();
        companyDto.setContacts(contacts);

        //  WHEN
        when(repository.findById(companyId)).thenReturn(Optional.of(companyDto));
        when(contactService.find(contactId)).thenReturn(contactDto);

        //  THEN / ASSERT
        ContactAlreadyAddedException e = assertThrows(ContactAlreadyAddedException.class, () -> service.contactToCompany(companyId, contactId));
        assertThat(e.getMessage()).isEqualTo(format("Contact with id [%s] already added in company [%s]", contactId, companyId));
    }

    @Test
    void createCompany() {
        // GIVEN
        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Company company = new Company(
                address,
                "0123456789"
        );

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setNumber("0123456789");
        companyDto.setAddress(addressDto);

        //  WHEN
        when(repository.saveAndFlush(any(CompanyDto.class))).thenReturn(companyDto);

        //  THEN
        Company companyCreated = service.createCompany(company);

        //  ASSERT
        assertThat(companyCreated.getId()).isEqualTo(1);
    }

    @Test
    void createCompany_alreadyExists() {
        // GIVEN
        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Company company = new Company(
                address,
                "0123456789"
        );

        //  WHEN
        when(repository.findByNumber("0123456789")).thenReturn(Optional.of(mock(CompanyDto.class)));

        //  THEN / ASSERT
        CompanyAlreadyExistsException e = assertThrows(CompanyAlreadyExistsException.class, () -> service.createCompany(company));
        assertThat(e.getMessage()).isEqualTo("Company with number [0123456789] already exists");
    }

    @Test
    void getCompanyByNumber() {
        //  GIVEN
        String number = "0123456789";

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setNumber(number);
        companyDto.setAddress(addressDto);

        //  WHEN
        when(repository.findByNumber(number)).thenReturn(Optional.of(companyDto));

        //  THEN
        Company company = service.getCompanyByNumber(number);

        //  ASSERT
        assertThat(company).isEqualTo(
                CompanyMapper.INSTANCE.mapToCompany(companyDto)
        );
    }

    @Test
    void getCompanyByNumber_notFound() {
        //  GIVEN
        String number = "0123456789";

        //  WHEN
        when(repository.findByNumber(number)).thenReturn(Optional.empty());

        //  THEN / ASSERT
        CompanyNumberNotFoundException e = assertThrows(CompanyNumberNotFoundException.class, () -> service.getCompanyByNumber(number));
        assertThat(e.getMessage()).isEqualTo("Company with number [0123456789] not found");
    }

    @Test
    void listCompanies() {
        //  GIVEN
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setNumber("0123456789");
        companyDto.setAddress(addressDto);

        List<CompanyDto> companies = List.of(companyDto);

        //  WHEN
        when(repository.findAll()).thenReturn(companies);

        //  THEN
        List<Company> companiesFound = service.listCompanies();

        //  ASSERT
        assertThat(companiesFound).hasSize(1);
    }

    @Test
    void updateCompany() {
        //  GIVEN
        ArgumentCaptor<CompanyDto> companyDtoArgumentCaptor = ArgumentCaptor.forClass(CompanyDto.class);

        Long id = 1L;
        String number = "0123456789";

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Company company = new Company(
                address,
                number
        );

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setNumber("0123456789");
        companyDto.setAddress(addressDto);

        //  WHEN
        when(repository.findById(id)).thenReturn(Optional.of(companyDto));
        when(repository.findByNumber(number)).thenReturn(Optional.empty());
        when(repository.saveAndFlush(companyDtoArgumentCaptor.capture())).thenReturn(companyDto);

        //  THEN
        Company companyUpdated = service.updateCompany(id, company);

        //  ASSERT
        assertThat(companyDtoArgumentCaptor.getValue().getId()).isEqualTo(id);
        assertThat(companyUpdated).isEqualTo(
                CompanyMapper.INSTANCE.mapToCompany(companyDto)
        );
    }

    @Test
    void updateCompany_notFound() {
        //  GIVEN
        Long id = 1L;

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Company company = new Company(
                address,
                "0123456789"
        );

        //  WHEN
        when(repository.findById(id)).thenReturn(Optional.empty());

        //  THEN / ASSERT
        CompanyNotFoundException e = assertThrows(CompanyNotFoundException.class, () -> service.updateCompany(id, company));
        assertThat(e.getMessage()).isEqualTo(format("Company with id [%s] not found", id));
    }

    @Test
    void updateCompany_alreadyExists() {
        //  GIVEN
        Long id = 1L;
        String number = "0123456789";

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Company company = new Company(
                address,
                number
        );

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(2L);
        companyDto.setNumber("0123456789");
        companyDto.setAddress(addressDto);

        //  WHEN
        when(repository.findById(id)).thenReturn(Optional.of(companyDto));
        when(repository.findByNumber(number)).thenReturn(Optional.of(companyDto));

        //  THEN / ASSERT
        CompanyAlreadyExistsException e = assertThrows(CompanyAlreadyExistsException.class, () -> service.updateCompany(id, company));
        assertThat(e.getMessage()).isEqualTo(format("Company with number [%s] already exists", number));
    }
}