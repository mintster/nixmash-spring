package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.model.Contact;

import java.util.List;

public interface ContactService {
    List<Contact> findAll();
    List<Contact> findByFirstName(String firstName);
    List<Contact> findByFirstNameAndLastName(String firstName, String lastName);
    List<Contact> getContactsWithDetail();
    void save(Contact contact);
    public Contact update(ContactDTO updated) throws NotFoundException;

    Contact findById(Long ID);
    Contact getContactByEmail(String email);
    Contact getContactByIdWithDetail(Long ID);
}
