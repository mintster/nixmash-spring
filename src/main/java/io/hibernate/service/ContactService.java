package io.hibernate.service;


import io.hibernate.model.Contact;

import java.util.List;


/**
 * Contact service interface.
 *
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface ContactService {

    /**
     * Creates the given contact in the persistent store.
     *
     * @param contact
     *            contact to create
     * @throws IllegalArgumentException
     *             if <code>contact</code> is <code>null</code>
     */
    void createContact(Contact contact);

    /**
     * Returns a list containing all contacts. Returns an empty list if there aren't any contacts.
     *
     * @return list of all contacts
     */
    List<Contact> getContacts();
    List<Contact> getContactsWithDetail();
    List<Contact> getContactsByEmail(String email);

    /**
     * Returns the contact having the given ID, or <code>null</code> if no such contact exists.
     *
     * @param id
     *            contact ID
     * @return contact having the given ID
     */
    Contact getContact(Long id);

    void updateContact(Contact contact);

    /**
     * Deletes the contact having the given ID.
     *
     * @param id
     *            contact ID
     */
    void deleteContact(Long id);
}