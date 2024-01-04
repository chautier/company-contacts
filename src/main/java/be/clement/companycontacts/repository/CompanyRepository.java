package be.clement.companycontacts.repository;

import be.clement.companycontacts.dto.CompanyDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDto, Long> {

    Optional<CompanyDto> findByNumber(String number);

}
