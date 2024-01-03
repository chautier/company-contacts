package be.clement.companycontacts.repository;

import be.clement.companycontacts.dto.AddressDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressDto, Long> {
}
