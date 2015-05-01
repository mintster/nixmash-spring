package com.nixmash.springdata.jpa.service.impl;

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.model.ContactEntity;
import com.nixmash.springdata.jpa.repository.ContactEntityRepository;
import com.nixmash.springdata.jpa.service.ContactEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Service("entityContactService")
@Transactional(value = "jpaTransactionManager")
public class ContactEntityServiceImpl implements ContactEntityService {
    @Autowired
    private ContactEntityRepository contactRepository;

    @Transactional(value = "jpaTransactionManager", readOnly=true)
    public List<ContactEntity> findAll() {
        return Lists.newArrayList(contactRepository.findAll());
    }

    @Transactional(value = "jpaTransactionManager", readOnly=true)
    public List<ContactEntity> findByFirstName(String firstName) {
        return contactRepository.findByFirstName(firstName);
    }

    @Transactional(value = "jpaTransactionManager", readOnly=true)
    public List<ContactEntity> findByFirstNameAndLastName(
        String firstName, String lastName) {
        return contactRepository.findByFirstNameAndLastName(
                firstName, lastName);
    }

    @Transactional(value = "jpaTransactionManager", readOnly=true)
    public List<ContactEntity> getContactsWithDetail() {
        return contactRepository.findAllWithDetail();
    }

}
