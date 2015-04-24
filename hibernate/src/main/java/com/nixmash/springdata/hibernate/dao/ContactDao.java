package com.nixmash.springdata.hibernate.dao;


import com.nixmash.springdata.hibernate.model.Contact;

import java.util.List;

public interface ContactDao extends Dao<Contact> {

    List<Contact> findByEmail(String email);
    List<Contact> findAllWithDetail();
    Contact findById(Long id);
}
