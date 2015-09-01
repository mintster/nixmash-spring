package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.ContactPhone;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactPhoneRepository extends CrudRepository<ContactPhone, Long> {

    ContactPhone findByContactPhoneId(Long id);
    List<ContactPhone> findByContact_ContactId(Long id);

}
