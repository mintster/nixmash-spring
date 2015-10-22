package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.model.validators.ExtendedEmailValidator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.AccessType.FIELD;
import static javax.persistence.AccessType.PROPERTY;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 5/11/15
 * Time: 3:25 PM
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "contacts")
@Access(PROPERTY)
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

    @Transient
    public String getFullName() {
        return this.firstName + ' ' + this.lastName;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "contact_id")
    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }


    @Column(name = "created_by_user", nullable = false)
    @CreatedBy
    private String createdByUser;
    
    @Column(name = "creation_time", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @CreatedDate
    private ZonedDateTime creationTime;
    
    @Column(name = "modified_by_user", nullable = false)
    @LastModifiedBy
    private String modifiedByUser;
    
    @Column(name = "modification_time")
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @LastModifiedDate
    private ZonedDateTime modificationTime;

    @Basic
    @Column(name = "first_name", nullable = false, insertable = true, updatable = true,
            length = MAX_LENGTH_FIRST_NAME)
    @NotEmpty
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, insertable = true, updatable = true,
            length = MAX_LENGTH_LAST_NAME)
    @NotEmpty
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

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setModifiedByUser(String modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public void setModificationTime(ZonedDateTime modificationTime) {
        this.modificationTime = modificationTime;
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

    public String getCreatedByUser() {
        return createdByUser;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public String getModifiedByUser() {
        return modifiedByUser;
    }

    public ZonedDateTime getModificationTime() {
        return modificationTime;
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
                .append("createdByUser", this.getCreatedByUser())
                .append("creationTime", this.getCreationTime())
                .append("modifiedByUser", this.getModifiedByUser())
                .append("modificationTime", this.getModificationTime())
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
