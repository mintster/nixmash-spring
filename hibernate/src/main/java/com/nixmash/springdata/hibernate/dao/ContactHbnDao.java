package com.nixmash.springdata.hibernate.dao;

import com.nixmash.springdata.hibernate.model.Contact;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public class ContactHbnDao extends AbstractHbnDao<Contact> implements ContactDao {


    @Override
    @SuppressWarnings("unchecked")
    @Transactional(value = "hibernateTransactionManager", readOnly=true)
    public List<Contact> findByEmail(String email) {
        return getSession()
                .getNamedQuery("Contact.findContactsByEmail")
                .setString("email", "%" + email + "%")
                .list();
    }


    @Override
    @SuppressWarnings("unchecked")
    @Transactional(value = "hibernateTransactionManager", readOnly=true)
    public List<Contact> findAllWithDetail() {
        return getSession()
                .getNamedQuery("Contact.findAllWithDetail")
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(value = "hibernateTransactionManager", readOnly=true)
    public Contact findById(Long id) {
        return (Contact) getSession()
                .getNamedQuery("Contact.findById")
                .setParameter("id", id).uniqueResult();
    }
}
