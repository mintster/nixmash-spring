package com.nixmash.springdata.hbn.dao;


import com.nixmash.springdata.model.Contact;

import java.util.List;

public interface ContactDao extends Dao<Contact> {

    List<Contact> findByEmail(String email);
    List<Contact> findAllWithDetail();
    Contact findById(Long id);
}
