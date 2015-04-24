package com.nixmash.springdata.jpa.service.impl;

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.repository.ContactRepository;
import com.nixmash.springdata.jpa.service.ContactJpaService;
import com.nixmash.springdata.jpa.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Service("jpaContactService")
@Transactional(value = "jpaTransactionManager")
public class ContactJpaServiceImpl implements ContactJpaService {
    @Autowired
    private ContactRepository contactRepository;

    @Transactional(value = "jpaTransactionManager", readOnly=true)
    public List<Contact> findAll() {
        return Lists.newArrayList(contactRepository.findAll());
    }

    @Transactional(value = "jpaTransactionManager", readOnly=true)
    public List<Contact> findByFirstName(String firstName) {
        return contactRepository.findByFirstName(firstName);
    }

    @Transactional(value = "jpaTransactionManager", readOnly=true)
    public List<Contact> findByFirstNameAndLastName(
        String firstName, String lastName) {
        return contactRepository.findByFirstNameAndLastName(
            firstName, lastName);
    }
}
