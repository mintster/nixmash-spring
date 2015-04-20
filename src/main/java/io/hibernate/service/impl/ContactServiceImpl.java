package io.hibernate.service.impl;
/*
 * Copyright (c) 2013 Manning Publications Co.
 *
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */

import io.hibernate.dao.ContactDao;
import io.hibernate.model.Contact;
import io.hibernate.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.util.Assert.notNull;

@Service(value="contactService")
@Transactional
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;

    @Override
    public void createContact(Contact contact) {
        notNull(contact, "contact can't be null");
        contactDao.create(contact);
    }

    @Override
    public List<Contact> getContacts() {
        return contactDao.getAll();
    }

    @Override
    public List<Contact> getContactsWithDetail() {
        return contactDao.findAllWithDetail();
    }

    @Override
    public List<Contact> getContactsByEmail(String email) {
        return contactDao.findByEmail(email);
    }

    @Override
    public Contact getContact(Long id) {
        notNull(id, "id can't be null");
        return contactDao.findById(id);
    }

    @Override
    public void updateContact(Contact contact) {
        notNull(contact, "contact can't be null");
        contactDao.update(contact);
    }

    @Override
    public void deleteContact(Long id) {
        notNull(id, "id can't be null");
        contactDao.deleteById(id);
    }
}