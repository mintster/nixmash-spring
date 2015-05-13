package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.Contact;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactRepository extends CrudRepository<Contact, Long> {

    List<Contact> findByFirstName(String firstName);
    List<Contact> findByFirstNameAndLastName(String firstName, String lastName);
    List<Contact> findAllWithDetail();
    Contact findByEmail(String email);

//    @Query("select distinct c from Contact c left join fetch " +
//            "c.contactPhones p left join fetch c.hobbies h where c.contactId = ?1")
    Contact findByContactIdWithDetail(Long ID);
}
