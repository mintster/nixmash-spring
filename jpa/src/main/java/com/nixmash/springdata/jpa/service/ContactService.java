package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.dto.HobbyDTO;
import com.nixmash.springdata.jpa.exceptions.ContactNotFoundException;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactPhone;
import com.nixmash.springdata.jpa.model.Hobby;

import java.util.List;

public interface ContactService {

    // region Contacts -------------------------------------- */

    List<Contact> findAll();
    List<Contact> findByFirstName(String firstName);
    List<Contact> findByFirstNameAndLastName(String firstName, String lastName);
    List<Contact> getContactsWithDetail();
    List<Contact> searchByLastName(String lastName);

    Contact add(ContactDTO added);
    Contact update(ContactDTO updated) throws ContactNotFoundException;

    Contact findContactById(Long ID) throws ContactNotFoundException;
    Contact getContactByEmail(String email);
    Contact getContactByIdWithDetail(Long ID);
    Contact deleteById(Long id) throws ContactNotFoundException;
    Contact removeHobby(ContactDTO updated, Long hobbyId) throws ContactNotFoundException;

    // endregion

    // region Contact Phones -------------------------------------- */

    List<ContactPhone> findContactPhonesByContactId(Long contactId);

    // endregion

    // region Hobbies --------------------------------------- */

    Hobby addNewHobby(HobbyDTO hobbyDTO);
    Hobby updateHobbyTitle(HobbyDTO hobbyDTO) throws ContactNotFoundException;
    List<Hobby> findAllContacts();
    Hobby findByHobbyTitle(String hobbyTitle);

    // endregion
}
