package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.common.ExtendedEmailValidator;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 5/11/15
 * Time: 3:25 PM
 */
@Entity
@Table(name = "contacts")
public class Contact implements Serializable {
    private Long contactId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private int version;
    private Set<ContactPhone> contactPhones;
    private Set<Hobby> hobbies;

    private static final long serialVersionUID = 447728202717826028L;

    public static final int MAX_LENGTH_EMAIL_ADDRESS = 100;
    public static final int MAX_LENGTH_FIRST_NAME = 40;
    public static final int MAX_LENGTH_LAST_NAME = 40;

    @Transient
    public boolean isNew() {
        return (this.contactId == null);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "contact_id", nullable = false, insertable = true, updatable = true,
            length = MAX_LENGTH_EMAIL_ADDRESS)
    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    @Basic
    @Column(name = "first_name", nullable = false, insertable = true, updatable = true,
            length = MAX_LENGTH_FIRST_NAME)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, insertable = true, updatable = true,
            length = MAX_LENGTH_LAST_NAME)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @NotNull @Past
    @Column(name = "birth_date",
            nullable = true,
            insertable = true,
            updatable = true)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Basic
    @ExtendedEmailValidator
    @Length(max = Contact.MAX_LENGTH_EMAIL_ADDRESS)
    @Column(name = "email", nullable = false, insertable = true, updatable = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Version
    @Column(name = "version", nullable = false, insertable = true, updatable = true, columnDefinition = "int default 0")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contact")
    public Set<ContactPhone> getContactPhones() {
        return contactPhones;
    }

    public void setContactPhones(Set<ContactPhone> contactPhones) {
        this.contactPhones = contactPhones;
    }

    public void addContactPhone(ContactPhone contactPhone) {
        contactPhone.setContact(this);
        getContactPhones().add(contactPhone);
    }

    @ManyToMany
    @JoinTable(name = "contact_hobby_ids",
            joinColumns = @JoinColumn(name = "contact_id",
                    referencedColumnName = "contact_id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "hobby_id",
                    referencedColumnName = "hobby_id",
                    nullable = false))
    public Set<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getContactId())
                .append("new", this.isNew())
                .append("lastName", this.getLastName())
                .append("firstName", this.getFirstName())
                .append("email", this.getEmail())
                .append("birthDate", this.getBirthDate())
                .toString();
    }

    public void update(final String firstName, final String lastName, final String emailAddress, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = emailAddress;
        this.birthDate = birthDate;
    }

    public static Builder getBuilder(String firstName, String lastName, String email) {
        return new Builder(firstName, lastName, email);
    }

    public static class Builder {

        private Contact built;

        public Builder(String firstName, String lastName, String email) {
            built = new Contact();
            built.firstName = firstName;
            built.lastName = lastName;
            built.email = email;
        }


        public Builder birthDate(Date birthDate) {
            built.birthDate = birthDate;
            return this;
        }

        public Contact build() {
            return built;
        }
    }


}
