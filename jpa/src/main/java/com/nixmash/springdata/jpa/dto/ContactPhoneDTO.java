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
    @Length(max = ContactPhone.MAX_LENGTH_TEL_TYPE)
    private String telType;

    @NotEmpty
    @Length(max = ContactPhone.MAX_LENGTH_TEL_NUMBER)
    private String telNumber;


    public ContactPhoneDTO() {

    }

//    public ContactPhoneDTO(Long contactPhoneId, Long contactId, String telType, String telNumber) {
//        this.contactPhoneId = contactPhoneId;
//        this.contactId = contactId;
//        this.telType = telType;
//        this.telNumber = telNumber;
//    }

    public ContactPhoneDTO(ContactPhone contactPhone) {
        this.contactPhoneId = contactPhone.getContactPhoneId();
        this.contactId = contactPhone.getContact().getContactId();
        this.telType = contactPhone.getTelType();
        this.telNumber = contactPhone.getTelNumber();
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

    public String getTelType() {
        return telType;
    }

    public void setTelType(String telType) {
        this.telType = telType;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
