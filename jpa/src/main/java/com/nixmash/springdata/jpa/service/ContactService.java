package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactPhone;

import java.util.List;

public interface ContactService {

    // region Contacts

    List<Contact> findAll();
    List<Contact> findByFirstName(String firstName);
    List<Contact> findByFirstNameAndLastName(String firstName, String lastName);
    List<Contact> getContactsWithDetail();
    Contact add(ContactDTO added);
    Contact update(ContactDTO updated) throws NotFoundException;

    Contact findById(Long ID);
    Contact getContactByEmail(String email);
    Contact getContactByIdWithDetail(Long ID);
    Contact deleteById(Long id) throws NotFoundException;

    // endregion

    // region Contact Phones -------------------------------------- */

    List<ContactPhone> findContactPhonesByContactId(Long contactId);

    // endregion
}
