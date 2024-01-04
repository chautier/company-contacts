package be.clement.companycontacts.dto.mapper;

import be.clement.companycontacts.dto.ContactDto;
import be.clement.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    Contact mapToContact(ContactDto contactDto);

    ContactDto mapToContactDto(Contact contact);

    default List<Contact> mapToListOfContact(List<ContactDto> contacts) {
        return contacts.stream()
                .map(this::mapToContact)
                .toList();
    }
}
