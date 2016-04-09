package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.validators.ExtendedEmailValidator;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

public class ContactDTO {

    private Long contactId;
    private Set<ContactPhoneDTO> contactPhones;
    private Set<HobbyDTO> hobbies;


    @ExtendedEmailValidator
    @Length(max = Contact.MAX_LENGTH_EMAIL_ADDRESS)
    private String email;

    @NotEmpty
    @Length(max = Contact.MAX_LENGTH_FIRST_NAME)
    private String firstName;

    @NotEmpty
    @Length(max = Contact.MAX_LENGTH_LAST_NAME)
    private String lastName;


    private Date birthDate;

    private boolean updateChildren = true;

    private String createdByUser;
    private ZonedDateTime creationTime;
    private String modifiedByUser;
    private ZonedDateTime modificationTime;

    public ContactDTO() {

    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
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


    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<ContactPhoneDTO> getContactPhones() {
        return contactPhones;
    }

    public void setContactPhones(Set<ContactPhoneDTO> contactPhones) {
        this.contactPhones = contactPhones;
    }


    public Set<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Set<HobbyDTO> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public boolean isNew() {
        return (this.contactId == null);
    }


    public boolean isUpdateChildren() {
        return updateChildren;
    }

    public void setUpdateChildren(boolean updateChildren) {
        this.updateChildren = updateChildren;
    }

}
