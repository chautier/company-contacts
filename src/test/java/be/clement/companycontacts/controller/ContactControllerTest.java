package be.clement.companycontacts.controller;

import be.clement.companycontacts.exception.ContactNotFoundException;
import be.clement.companycontacts.exception.ContactRequiredNumberForFreelanceException;
import be.clement.companycontacts.service.ContactService;
import be.clement.model.Address;
import be.clement.model.Contact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ContactService contactService;

    @Test
    void createContact() throws Exception {
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

        Contact contactCreated = new Contact(
                address,
                "Last Name",
                "First Name",
                Boolean.FALSE
        );
        contactCreated.setId(1);

        //  WHEN
        when(contactService.createContact(contact)).thenReturn(contactCreated);

        //  THEN
        String result = mvc.perform(
                        post("/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(contact))
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //  ASSERT
        assertThat(result).isEqualTo(mapper.writeValueAsString(contactCreated));
    }

    @Test
    void createContact_badRequest() throws Exception {
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
        contact.setNumber("BAD_NUMBER");

        //  WHEN
        when(contactService.createContact(contact)).thenThrow(ContactRequiredNumberForFreelanceException.class);

        //  THEN
        mvc.perform(
                post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(contact))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void listContacts() throws Exception {
        //  GIVEN
        List<Contact> contacts = List.of(mock(Contact.class));

        //  WHEN
        when(contactService.listContacts()).thenReturn(contacts);

        //  THEN
        String result = mvc.perform(
                        get("/contacts")
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //  ASSERT
        assertThat(result).isEqualTo(mapper.writeValueAsString(contacts));
    }

    @Test
    void updateContact() throws Exception {
        // GIVEN
        Long contactId = 1L;

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Contact contact = new Contact(
                address,
                "Last Name",
                "First Nameeeeeeeeee",
                Boolean.FALSE
        );

        Contact contactUpdated = new Contact(
                address,
                "Last Name",
                "First Nameeeeeeeeee",
                Boolean.FALSE
        );
        contactUpdated.setId(1);

        //  WHEN
        when(contactService.updateContact(contactId, contact)).thenReturn(contactUpdated);

        //  THEN
        String result = mvc.perform(
                        put(format("/contacts/%s", contactId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(contact))
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //  ASSERT
        assertThat(result).isEqualTo(mapper.writeValueAsString(contactUpdated));
    }

    @Test
    void updateContact_notFound() throws Exception {
        // GIVEN
        Long contactId = 1L;

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Contact contact = new Contact(
                address,
                "Last Name",
                "First Nameeeeeeeeee",
                Boolean.FALSE
        );

        //  WHEN
        when(contactService.updateContact(contactId, contact)).thenThrow(ContactNotFoundException.class);

        //  THEN
        mvc.perform(
                put(format("/contacts/%s", contactId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(contact))
        ).andExpect(status().isNotFound());
    }

    @Test
    void deleteContact() throws Exception {
        // GIVEN
        Long contactId = 1L;

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Contact contact = new Contact(
                address,
                "Last Name",
                "First Nameeeeeeeeee",
                Boolean.FALSE
        );

        //  THEN
        mvc.perform(
                delete(format("/contacts/%s", contactId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(contact))
        ).andExpect(status().isNoContent());
    }

    @Test
    void deleteContact_notFound() throws Exception {
        // GIVEN
        Long contactId = 1L;

        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Contact contact = new Contact(
                address,
                "Last Name",
                "First Nameeeeeeeeee",
                Boolean.FALSE
        );

        //  WHEN
        doThrow(ContactNotFoundException.class).when(contactService).deleteContact(contactId);

        //  THEN
        mvc.perform(
                delete(format("/contacts/%s", contactId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(contact))
        ).andExpect(status().isNotFound());
    }
}