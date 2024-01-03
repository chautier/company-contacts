package be.clement.companycontacts.repository;

import be.clement.companycontacts.dto.ContactDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactDto, Long> {
}
