package io.hibernate.dao;


import io.hibernate.model.Contact;

import java.util.List;

public interface ContactDao extends Dao<Contact> {

    List<Contact> findByEmail(String email);
    List<Contact> findAllWithDetail();
    Contact findById(Long id);
}
