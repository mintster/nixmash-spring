package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.model.ContactEntity;

import java.util.List;

public interface ContactService {
    List<ContactEntity> findAll();
    List<ContactEntity> findByFirstName(String firstName);
    List<ContactEntity> findByFirstNameAndLastName(String firstName, String lastName);
    List<ContactEntity> getContactsWithDetail();
    void save(ContactEntity contact);
    ContactEntity findById(Long ID);
}
