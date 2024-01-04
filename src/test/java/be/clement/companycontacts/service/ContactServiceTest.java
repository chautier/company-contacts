package be.clement.companycontacts.service;

import be.clement.companycontacts.dto.AddressDto;
import be.clement.companycontacts.dto.ContactDto;
import be.clement.companycontacts.dto.mapper.ContactMapper;
import be.clement.companycontacts.exception.ContactNotFoundException;
import be.clement.companycontacts.exception.ContactRequiredNumberForFreelanceException;
import be.clement.companycontacts.repository.ContactRepository;
import be.clement.model.Address;
import be.clement.model.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository repository;

    @InjectMocks
    private ContactService service;

    @Test
    void createContact() {
        // GIVEN
        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Contact contact = new Contact(
                address,
                "Last Name",
                "First Name",
                Boolean.FALSE
        );

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        ContactDto contactDto = new ContactDto();
        contactDto.setId(1L);
        contactDto.setFreelance(Boolean.FALSE);
        contactDto.setFirstName("First Name");
        contactDto.setLastName("Last Name");
        contactDto.setAddress(addressDto);

        //  WHEN
        when(repository.saveAndFlush(any(ContactDto.class))).thenReturn(contactDto);

        //  THEN
        Contact contactCreated = service.createContact(contact);

        //  ASSERT
        assertThat(contactCreated.getId()).isEqualTo(1);
    }

    @Test
    void createContact_freelanceMissingNumber() {
        // GIVEN
        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Contact contact = new Contact(
                address,
                "Last Name",
                "First Name",
                Boolean.TRUE
        );

        //  THEN / ASSERT
        ContactRequiredNumberForFreelanceException e = assertThrows(ContactRequiredNumberForFreelanceException.class, () -> service.createContact(contact));
        assertThat(e.getMessage()).isEqualTo("VAT Number is required for freelance");
    }

    @Test
    void deleteContact() {
        //  GIVEN
        Long id = 1L;

        //  WHEN
        when(repository.findById(id)).thenReturn(Optional.of(mock(ContactDto.class)));

        //  THEN
        service.deleteContact(id);

        //  ASSERT
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deleteContact_notFound() {
        //  GIVEN
        Long id = 1L;

        //  THEN / ASSERT
        ContactNotFoundException e = assertThrows(ContactNotFoundException.class, () -> service.deleteContact(id));
        assertThat(e.getMessage()).isEqualTo(format("Contact with id [%s] not found", id));
    }

    @Test
    void listContacts() {
        //  GIVEN
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        ContactDto contactDto = new ContactDto();
        contactDto.setId(1L);
        contactDto.setFreelance(Boolean.FALSE);
        contactDto.setFirstName("First Name");
        contactDto.setLastName("Last Name");
        contactDto.setAddress(addressDto);

        List<ContactDto> contacts = List.of(contactDto);

        //  WHEN
        when(repository.findAll()).thenReturn(contacts);

        //  THEN
        List<Contact> contactsFound = service.listContacts();

        //  ASSERT
        assertThat(contactsFound).hasSize(1);
    }

    @Test
    void updateContact() {
        //  GIVEN
        ArgumentCaptor<ContactDto> contactDtoArgumentCaptor = ArgumentCaptor.forClass(ContactDto.class);

        Long id = 1L;

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Contact contact = new Contact(
                address,
                "Last Name",
                "First Name",
                Boolean.FALSE
        );

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        ContactDto contactDto = new ContactDto();
        contactDto.setId(1L);
        contactDto.setFreelance(Boolean.FALSE);
        contactDto.setFirstName("First Name");
        contactDto.setLastName("Last Name");
        contactDto.setAddress(addressDto);

        //  WHEN
        when(repository.findById(id)).thenReturn(Optional.of(contactDto));
        when(repository.saveAndFlush(contactDtoArgumentCaptor.capture())).thenReturn(contactDto);

        //  THEN
        Contact contactUpdated = service.updateContact(id, contact);

        //  ASSERT
        assertThat(contactDtoArgumentCaptor.getValue().getId()).isEqualTo(id);
        assertThat(contactUpdated).isEqualTo(
                ContactMapper.INSTANCE.mapToContact(contactDto)
        );
    }

    @Test
    void updateContact_notFound() {
        //  GIVEN
        Long id = 1L;

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Contact contact = new Contact(
                address,
                "Last Name",
                "First Name",
                Boolean.FALSE
        );

        //  THEN / ASSERT
        ContactNotFoundException e = assertThrows(ContactNotFoundException.class, () -> service.updateContact(id, contact));
        assertThat(e.getMessage()).isEqualTo(format("Contact with id [%s] not found", id));
    }

    @Test
    void find() {
        //  GIVEN
        Long id = 1L;

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("Street");
        addressDto.setNumber("1");
        addressDto.setCity("City");
        addressDto.setZip("1234");
        ContactDto contactDto = new ContactDto();
        contactDto.setId(1L);
        contactDto.setFreelance(Boolean.FALSE);
        contactDto.setFirstName("First Name");
        contactDto.setLastName("Last Name");
        contactDto.setAddress(addressDto);

        //  WHEN
        when(repository.findById(id)).thenReturn(Optional.of(contactDto));

        //  THEN
        ContactDto contactFound = service.find(id);

        //  ASSERT
        assertThat(contactFound).isEqualTo(contactDto);
    }

    @Test
    void find_notFound() {
        //  GIVEN
        Long id = 1L;

        //  THEN / ASSERT
        ContactNotFoundException e = assertThrows(ContactNotFoundException.class, () -> service.find(id));
        assertThat(e.getMessage()).isEqualTo(format("Contact with id [%s] not found", id));
    }

}