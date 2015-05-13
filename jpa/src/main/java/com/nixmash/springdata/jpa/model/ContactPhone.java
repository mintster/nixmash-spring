package com.nixmash.springdata.jpa.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 5/11/15
 * Time: 3:25 PM
 */
@Entity
@Table(name = "contact_phones")
public class ContactPhone {
    private Long contactPhoneId;
    private String telType;
    private String telNumber;
    private int version;
    private Contact contact;

    public static final int MAX_LENGTH_TEL_TYPE = 20;
    public static final int MAX_LENGTH_TEL_NUMBER = 20;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "contact_phone_id", nullable = false, insertable = true, updatable = true)
    public Long getContactPhoneId() {
        return contactPhoneId;
    }

    public void setContactPhoneId(Long contactPhoneId) {
        this.contactPhoneId = contactPhoneId;
    }

    @Basic
    @Column(name = "tel_type", nullable = false, insertable = true, updatable = true, length = MAX_LENGTH_TEL_TYPE)
    public String getTelType() {
        return telType;
    }

    public void setTelType(String telType) {
        this.telType = telType;
    }

    @Basic
    @Column(name = "tel_number", nullable = false, insertable = true, updatable = true, length = MAX_LENGTH_TEL_NUMBER)
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    @Basic
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

        ContactPhone that = (ContactPhone) o;

        if (contactPhoneId != that.contactPhoneId) return false;
        if (version != that.version) return false;
        if (telType != null ? !telType.equals(that.telType) : that.telType != null) return false;
        if (telNumber != null ? !telNumber.equals(that.telNumber) : that.telNumber != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Long result = contactPhoneId;
        result = 31 * result + (telType != null ? telType.hashCode() : 0);
        result = 31 * result + (telNumber != null ? telNumber.hashCode() : 0);
        return (int)((result >> 32) ^ result);
    }

    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "contact_id", nullable = false)
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "   Details: " +
                "id=" + contactPhoneId +
                ", telType='" + telType + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", version=" + version;
    }
}
