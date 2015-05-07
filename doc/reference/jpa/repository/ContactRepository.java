package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Contact;
import org.springframework.data.repository.CrudRepository;

import java.lang.Long;
import java.lang.Override;
import java.util.List;

public interface ContactRepository extends CrudRepository<Contact, Long> {
    List<Contact> findByFirstName(String firstName);

    List<Contact> findByFirstNameAndLastName(String firstName, String lastName);

    List<Contact> findAllWithDetail();

}
