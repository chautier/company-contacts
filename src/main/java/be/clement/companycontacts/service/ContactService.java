package be.clement.companycontacts.service;

import be.clement.companycontacts.dto.ContactDto;
import be.clement.companycontacts.dto.mapper.ContactMapper;
import be.clement.companycontacts.exception.ContactNotFoundException;
import be.clement.companycontacts.exception.ContactRequiredNumberForFreelanceException;
import be.clement.companycontacts.repository.ContactRepository;
import be.clement.model.Contact;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public be.clement.model.Contact createContact(Contact contact) {
        if (contact.getFreelance() && StringUtils.isEmpty(contact.getNumber())) {
            throw new ContactRequiredNumberForFreelanceException();
        }

        return ContactMapper.INSTANCE.mapToContact(
                repository.saveAndFlush(
                        ContactMapper.INSTANCE.mapToContactDto(contact)
                )
        );
    }

    public void deleteContact(Long id) {
        Optional<ContactDto> contactDto = repository.findById(id);

        if (contactDto.isEmpty()) {
            throw new ContactNotFoundException(id);
        }

        repository.deleteById(id);
    }

    public List<Contact> listContacts() {
        return ContactMapper.INSTANCE.mapToListOfContact(
                repository.findAll()
        );
    }

    public Contact updateContact(Long id, Contact contact) {
        Optional<ContactDto> contactDto = repository.findById(id);

        if (contactDto.isEmpty()) {
            throw new ContactNotFoundException(id);
        }

        contact.setId(Math.toIntExact(id));

        return ContactMapper.INSTANCE.mapToContact(
                repository.saveAndFlush(
                        ContactMapper.INSTANCE.mapToContactDto(contact)
                )
        );
    }
}
