package be.clement.companycontacts.controller;

import be.clement.companycontacts.exception.CompanyAlreadyExistsException;
import be.clement.companycontacts.exception.CompanyNotFoundException;
import be.clement.companycontacts.exception.ContactAlreadyAddedException;
import be.clement.companycontacts.service.CompanyService;
import be.clement.model.Address;
import be.clement.model.Company;
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
class CompanyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CompanyService companyService;

    @Test
    void contactToCompany() throws Exception {
        // GIVEN
        Long companyId = 1L;
        Long contactId = 2L;

        //  THEN
        mvc.perform(
                post(format("/companies/%s/contacts/%s", companyId, contactId))
        ).andExpect(status().isOk());
    }

    @Test
    void contactToCompany_notFound() throws Exception {
        // GIVEN
        Long companyId = 1L;
        Long contactId = 2L;

        //  WHEN
        doThrow(CompanyNotFoundException.class).when(companyService).contactToCompany(companyId, contactId);

        //  THEN
        mvc.perform(
                post(format("/companies/%s/contacts/%s", companyId, contactId))
        ).andExpect(status().isNotFound());
    }

    @Test
    void contactToCompany_badRequest() throws Exception {
        // GIVEN
        Long companyId = 1L;
        Long contactId = 2L;

        //  WHEN
        doThrow(ContactAlreadyAddedException.class).when(companyService).contactToCompany(companyId, contactId);

        //  THEN
        mvc.perform(
                post(format("/companies/%s/contacts/%s", companyId, contactId))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void createCompany() throws Exception {
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

        Company companyCreated = new Company(
                address,
                "0123456789"
        );
        companyCreated.setId(1);

        //  WHEN
        when(companyService.createCompany(company)).thenReturn(companyCreated);

        //  THEN
        String result = mvc.perform(
                        post("/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(company))
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //  ASSERT
        assertThat(result).isEqualTo(mapper.writeValueAsString(companyCreated));
    }

    @Test
    void createCompany_badRequest() throws Exception {
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
        when(companyService.createCompany(company)).thenThrow(CompanyAlreadyExistsException.class);

        //  THEN
        mvc.perform(
                post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(company))
        ).andExpect(status().isBadRequest());
    }

    @Test
    void getCompanyByNumber() throws Exception {
        //  GIVEN
        String number = "0123456789";

        Company company = mock(Company.class);

        //  WHEN
        when(companyService.getCompanyByNumber(number)).thenReturn(company);

        //  THEN
        String result = mvc.perform(
                        get(format("/companies/%s", number))
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //  ASSERT
        assertThat(result).isEqualTo(mapper.writeValueAsString(company));
    }

    @Test
    void getCompanyByNumber_notFound() throws Exception {
        //  GIVEN
        String number = "0123456789";

        //  WHEN
        when(companyService.getCompanyByNumber(number)).thenThrow(CompanyNotFoundException.class);

        //  THEN
        mvc.perform(
                get(format("/companies/%s", number))
        ).andExpect(status().isNotFound());
    }

    @Test
    void listCompanies() throws Exception {
        //  GIVEN
        List<Company> companies = List.of(mock(Company.class));

        //  WHEN
        when(companyService.listCompanies()).thenReturn(companies);

        //  THEN
        String result = mvc.perform(
                        get("/companies")
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //  ASSERT
        assertThat(result).isEqualTo(mapper.writeValueAsString(companies));
    }

    @Test
    void updateCompany() throws Exception {
        // GIVEN
        Long companyId = 1L;
        Address address = new Address(
                "Street",
                "Number",
                "City",
                "Zip"
        );
        Company company = new Company(
                address,
                "0123456788"
        );

        Company companyUpdated = new Company(
                address,
                "0123456788"
        );
        companyUpdated.setId(1);

        //  WHEN
        when(companyService.updateCompany(companyId, company)).thenReturn(companyUpdated);

        //  THEN
        String result = mvc.perform(
                        put(format("/companies/%s", companyId))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(company))
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //  ASSERT
        assertThat(result).isEqualTo(mapper.writeValueAsString(companyUpdated));
    }

    @Test
    void updateCompany_notFound() throws Exception {
        //  GIVEN
        Long companyId = 1L;
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
        when(companyService.updateCompany(companyId, company)).thenThrow(CompanyNotFoundException.class);

        //  THEN
        mvc.perform(
                put(format("/companies/%s", companyId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(company))
        ).andExpect(status().isNotFound());
    }

    @Test
    void updateCompany_badRequest() throws Exception {
        //  GIVEN
        Long companyId = 1L;
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
        when(companyService.updateCompany(companyId, company)).thenThrow(CompanyAlreadyExistsException.class);

        //  THEN
        mvc.perform(
                put(format("/companies/%s", companyId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(company))
        ).andExpect(status().isBadRequest());
    }
}