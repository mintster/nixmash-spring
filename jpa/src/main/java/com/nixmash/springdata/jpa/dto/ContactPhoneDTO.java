package com.nixmash.springdata.jpa.dto;

//import org.apache.commons.lang.builder.ToStringBuilder;
//import org.hibernate.validator.constraints.Email;
//import org.hibernate.validator.constraints.Length;
//import org.hibernate.validator.constraints.NotEmpty;

import com.nixmash.springdata.jpa.model.ContactPhone;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * A form object for contracts.
 * @author Petri Kainulainen
 */
public class ContactPhoneDTO {

    private Long contactPhoneId;
    private Long contactId;

    @NotEmpty
    @Length(max = ContactPhone.MAX_LENGTH_PHONE_TYPE)
    private String phoneType;

    @NotEmpty
    @Length(max = ContactPhone.MAX_LENGTH_PHONE_NUMBER)
    private String phoneNumber;


    public ContactPhoneDTO() {

    }

    public ContactPhoneDTO(String phoneType, String phoneNumber) {
        this.phoneType = phoneType;
        this.phoneNumber = phoneNumber;
    }

    public ContactPhoneDTO(ContactPhone contactPhone) {
        this.contactPhoneId = contactPhone.getContactPhoneId();
        this.contactId = contactPhone.getContact().getContactId();
        this.phoneType = contactPhone.getPhoneType();
        this.phoneNumber = contactPhone.getPhoneNumber();
    }

    public Long getContactPhoneId() {
        return contactPhoneId;
    }

    public void setContactPhoneId(Long contactPhoneId) {
        this.contactPhoneId = contactPhoneId;
    }


    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
