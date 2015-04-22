package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.model.Contact;

import java.util.List;

public interface ContactJpaService {
    List<Contact> findAll();
    List<Contact> findByFirstName(String firstName);
    List<Contact> findByFirstNameAndLastName(String firstName, String lastName);
}
