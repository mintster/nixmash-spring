package com.nixmash.springdata.hibernate.service;


import com.nixmash.springdata.hibernate.model.Contact;

import java.util.List;


/**
 * Contact service interface.
 *
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface ContactService {

    void createContact(Contact contact);
    List<Contact> getContacts();
    List<Contact> getContactsWithDetail();
    List<Contact> getContactsByEmail(String email);
    Contact getContact(Long id);
    void updateContact(Contact contact);
    void deleteContact(Long id);
}