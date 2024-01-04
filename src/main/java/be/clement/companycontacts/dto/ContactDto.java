package be.clement.companycontacts.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contact")
public class ContactDto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressDto address;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "freelance", nullable = false)
    private Boolean freelance;

    @Column(name = "number")
    private String number;  //  Required if it's a freelance

    @ManyToMany(mappedBy = "contacts")
    private Set<CompanyDto> companies = new HashSet<>();

    @PreRemove
    private void removeCompanies() {
        for (CompanyDto company : companies) {
            company.getContacts().remove(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto addressDto) {
        this.address = addressDto;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getFreelance() {
        return freelance;
    }

    public void setFreelance(Boolean freelance) {
        this.freelance = freelance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<CompanyDto> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<CompanyDto> companies) {
        this.companies = companies;
    }
}
