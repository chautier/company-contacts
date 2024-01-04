package be.clement.companycontacts.dto.mapper;

import be.clement.companycontacts.dto.CompanyDto;
import be.clement.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CompanyMapper {

    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    Company mapToCompany(CompanyDto companyDto);

    CompanyDto mapToCompanyDto(Company company);

    default List<Company> mapToListOfCompany(List<CompanyDto> companies) {
        return companies.stream()
                .map(this::mapToCompany)
                .toList();
    }
}
