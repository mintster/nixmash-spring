package io.hibernate.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by family on 6/8/2014.
 */

@Transactional
@Repository("contactDao")
@SuppressWarnings("unchecked")
public class ContactDaoImpl implements ContactDao {

    private Log log = LogFactory.getLog(ContactDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;


    @Transactional(readOnly=true)
    @Override
    public List<Contact> findAll() {
        return sessionFactory.getCurrentSession().
                createQuery("from Contact c").list();
    }

    @Override
    @Transactional(readOnly=true)
    public List<Contact> findAllWithDetail() {
        return sessionFactory.getCurrentSession().
                getNamedQuery("Contact.findAllWithDetail").list();
    }

    @Override
    @Transactional(readOnly=true)
    public Contact findById(Long id) {
        return (Contact) sessionFactory.getCurrentSession().
                getNamedQuery("Contact.findById").
                setParameter("id", id).uniqueResult();
    }

    @Override
    public Contact save(Contact contact) {
        sessionFactory.getCurrentSession().saveOrUpdate(contact);
        log.info("Contact saved with id: " + contact.getId());
        return contact;
    }

    @Override
    public void delete(Contact contact) {
        sessionFactory.getCurrentSession().delete(contact);
        log.info("Contact deleted with id: " + contact.getId());
    }
}
