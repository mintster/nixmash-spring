package com.nixmash.springdata.model;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by family on 6/8/2014.
 */
@Entity
@Table(name="contact_tel_detail")
public class ContactTelDetail implements Serializable {

    private Long id;
    private int version;
    private String telType;
    private String telNumber;
    private Contact contact;


    public ContactTelDetail() {
    }

    public ContactTelDetail(String telType, String telNumber) {
        this.telType=telType;
        this.telNumber=telNumber;
    }

    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Column(name="ID")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id= id;
    }

    @Version
    @Column(name="version")
    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version=version;
    }

    @Column(name="tel_type")
    public String getTelType() {
        return this.telType;
    }

    public void setTelType(String telType) {
        this.telType=telType;
    }

    @Column(name="tel_number")
    public String getTelNumber() {
        return this.telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber=telNumber;
    }


    @ManyToOne
    @JoinColumn(name="contact_id")
    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact=contact;
    }

    public String toString() {
        return "Contact Tel Detail - Id: " + id + ", Contact id: "
                + getContact().getId() + ", Type: "
                + telType + ", Number: " + telNumber;
    }

}