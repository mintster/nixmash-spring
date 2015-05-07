package com.nixmash.springdata.jpa.model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 4/29/15
 * Time: 1:24 PM
 */
@Entity
@Table(name = "contact_tel_detail", schema = "", catalog = "dev_hibernate")
public class ContactTelDetailEntity {
    private Long id;
    private String telType;
    private String telNumber;
    private int version;
    private ContactEntity contactEntity;

    public ContactTelDetailEntity() {
    }

    public ContactTelDetailEntity(String telType, String telNumber) {
        this.telType=telType;
        this.telNumber=telNumber;
    }

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "tel_type", nullable = false, insertable = true, updatable = true, length = 20)
    public String getTelType() {
        return telType;
    }

    public void setTelType(String telType) {
        this.telType = telType;
    }

    @Basic
    @Column(name = "tel_number", nullable = false, insertable = true, updatable = true, length = 20)
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    @Basic
    @Column(name = "version", nullable = false, insertable = true, updatable = true)
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ContactTelDetailEntity that = (ContactTelDetailEntity) o;
//
//        if (id != that.id) return false;
//        if (version != that.version) return false;
//        if (telType != null ? !telType.equals(that.telType) : that.telType != null) return false;
//        if (telNumber != null ? !telNumber.equals(that.telNumber) : that.telNumber != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        Long result = id;
//        result = 31 * result + (telType != null ? telType.hashCode() : 0);
//        result = 31 * result + (telNumber != null ? telNumber.hashCode() : 0);
//        result = 31 * result + version;
//        return result;
//    }

    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false)
    public ContactEntity getContactEntity() {
        return this.contactEntity;
    }

    public void setContactEntity(ContactEntity contactEntity) {
        this.contactEntity = contactEntity;
    }

    @Override
    public String toString() {
        return "   Details: " +
                "id=" + id +
                ", telType='" + telType + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", version=" + version;
    }
}
