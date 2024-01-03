package be.clement.companycontacts.controller;

import be.clement.api.ContactsApi;
import be.clement.companycontacts.service.ContactService;
import be.clement.model.Contact;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContactController implements ContactsApi {

    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Contact> createContact(Contact contact) {
        return new ResponseEntity<>(
                service.createContact(contact),
                HttpStatus.CREATED
        );
    }

    @Override
    public ResponseEntity<Void> deleteContact(Integer id) {
        service.deleteContact(Long.valueOf(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<Contact>> listContacts() {
        return new ResponseEntity<>(
                service.listContacts(),
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<Contact> updateContact(Integer id, Contact contact) {
        return new ResponseEntity<>(
                service.updateContact(Long.valueOf(id), contact),
                HttpStatus.OK
        );
    }
}
