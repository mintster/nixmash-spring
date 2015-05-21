package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 5/11/15
 * Time: 3:25 PM
 */
@Entity
@Table(name = "contacts")
public class Contact {
    private Long contactId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private int version;
    private Set<ContactPhone> contactPhones;
    private Set<Hobby> hobbies;


    public static final int MAX_LENGTH_EMAIL_ADDRESS = 100;
    public static final int MAX_LENGTH_FIRST_NAME = 40;
    public static final int MAX_LENGTH_LAST_NAME = 40;

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
    @Column(name = "birth_date", nullable = true, insertable = true, updatable = true)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Basic
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (contactId != contact.contactId) return false;
        if (version != contact.version) return false;
        if (firstName != null ? !firstName.equals(contact.firstName) : contact.firstName != null) return false;
        if (lastName != null ? !lastName.equals(contact.lastName) : contact.lastName != null) return false;
        if (birthDate != null ? !birthDate.equals(contact.birthDate) : contact.birthDate != null) return false;
        if (email != null ? !email.equals(contact.email) : contact.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Long result = contactId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + version;
        return (int) ((result >> 32) ^ result);
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
    @JoinTable(name = "contact_hobby_ids", joinColumns = @JoinColumn(name = "contact_id", referencedColumnName = "contact_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "hobby_id", referencedColumnName = "hobby_id", nullable = false))
    public Set<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    public String toString() {
        return "Contact - Id: " + contactId + ", First name: " + firstName
                + ", Last name: " + lastName +
                ", Email: " + email +
                ", Birthday: " + birthDate;
    }


    public void update(final String firstName, final String lastName, final String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = emailAddress;
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
