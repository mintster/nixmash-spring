package com.nixmash.springdata.jpa.service.impl;

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.dto.ContactPhoneDTO;
import com.nixmash.springdata.jpa.dto.HobbyDTO;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactPhone;
import com.nixmash.springdata.jpa.model.Hobby;
import com.nixmash.springdata.jpa.repository.ContactPhoneRepository;
import com.nixmash.springdata.jpa.repository.ContactRepository;
import com.nixmash.springdata.jpa.repository.HobbyRepository;
import com.nixmash.springdata.jpa.service.ContactService;
import com.nixmash.springdata.jpa.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

//@Repository
@Service("contactService")
@Transactional
public class ContactServiceImpl implements ContactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    // region Beans ------------------------- */

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactPhoneRepository contactPhoneRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @PersistenceContext
    private EntityManager em;

    // endregion

    // region Contacts ------------------------- */

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    public List<Contact> findAll() {
        return Lists.newArrayList(contactRepository.findAll());
    }

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    public List<Contact> findByFirstName(String firstName) {
        return contactRepository.findByFirstName(firstName);
    }

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    public List<Contact> findByFirstNameAndLastName(
            String firstName, String lastName) {
        return contactRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    public List<Contact> searchByLastName(String lastName) {
        return contactRepository.searchByLastName(lastName);
    }

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    public List<Contact> getContactsWithDetail() {
        return contactRepository.findAllWithDetail();
    }

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    public Contact getContactByIdWithDetail(Long ID) {
//        Contact contact = contactRepository.findOne(ID);
//        contact.getContactPhones();
//        contact.getHobbies();
//        return contact;
//        return em.createNamedQuery("Contact.findByContactIdWithDetail",
//                Contact.class)
//                .setParameter("contactId", ID)
//                .getSingleResult();
        return contactRepository.findByContactIdWithDetail(ID);
    }

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    public Contact findContactById(Long ID) {
        return contactRepository.findOne(ID);
    }

    public Contact getContactByEmail(String email) {
        return contactRepository.findByEmail(email);
    }


    @Transactional(rollbackFor = NotFoundException.class)
    @Override
    public Contact deleteById(Long id) throws NotFoundException {
        LOGGER.info("Deleting contact by id: {}", id);

        Contact deleted = findContactById(id);
        contactRepository.delete(deleted);

        LOGGER.info("Deleted contact: {}", deleted);
        return deleted;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    @Override
    public Contact update(ContactDTO contactDto) throws NotFoundException {
        LOGGER.info("Updating contact with information: {}", contactDto);

        Contact found = findContactById(contactDto.getContactId());

        // Update the contact information
        found.update(contactDto.getFirstName(), contactDto.getLastName(), contactDto.getEmail());
        // Update the contact phone
        if (found.getContactPhones() != null) {
            for (ContactPhoneDTO contactPhoneDTO : contactDto.getContactPhones()) {
                ContactPhone contactPhone = contactPhoneRepository.findByContactPhoneId(contactPhoneDTO.getContactPhoneId());
                if (contactPhone != null) {
                    contactPhone.update(contactPhoneDTO.getPhoneType(), contactPhoneDTO.getPhoneNumber());
                }
            }
        }

        if (contactDto.getHobbies() != null) {
            saveNewHobbiesToDatabase(contactDto);
        }

        if (contactDto.getHobbies() != null) {
            for (HobbyDTO hobbyDTO : contactDto.getHobbies()) {
                Hobby hobby = hobbyRepository.findByHobbyTitleIgnoreCase(hobbyDTO.getHobbyTitle());

                if (!found.getHobbies().contains(hobby))
                    found.getHobbies().add(hobby);
            }
        }

        return found;
    }

    @Transactional(rollbackFor = NotFoundException.class)
    @Override
    public Contact removeHobby(ContactDTO contactDto, Long hobbyId) throws NotFoundException {
        LOGGER.info("Removing contact hobby with information: {}", contactDto);

        Contact found = findContactById(contactDto.getContactId());
        Hobby hobby = hobbyRepository.findOne(hobbyId);

        if (found.getHobbies().contains(hobby))
            found.getHobbies().remove(hobby);

        return found;
    }

    @Transactional
    @Override
    public Contact add(ContactDTO contactDto) {
        LOGGER.info("Adding new contact with information: {}", contactDto);

        //Creates an instance of a Contact by using the builder pattern
        Contact contact = Contact.getBuilder(contactDto.getFirstName(),
                contactDto.getLastName(), contactDto.getEmail())
                .birthDate(contactDto.getBirthDate())
                .build();

        Contact saved = contactRepository.save(contact);

        if (contactDto.getContactPhones() != null) {
            for (ContactPhoneDTO contactPhoneDTO : contactDto.getContactPhones()) {
                ContactPhone contactPhone = ContactPhone.getBuilder(saved,
                        contactPhoneDTO.getPhoneType(),
                        contactPhoneDTO.getPhoneNumber())
                        .build();

                contactPhoneRepository.save(contactPhone);
            }
        }

        if (contactDto.getHobbies() != null) {
            saveNewHobbiesToDatabase(contactDto);
        }

        em.refresh(saved);

        if (contactDto.getHobbies() != null) {
            for (HobbyDTO hobbyDTO : contactDto.getHobbies()) {
                Hobby hobby = hobbyRepository.findByHobbyTitleIgnoreCase(hobbyDTO.getHobbyTitle());
                saved.getHobbies().add(hobby);
            }
        }

        return saved;

    }


    // endregion

    // region Contact Phones ---------------------- */

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    public List<ContactPhone> findContactPhonesByContactId(Long contactId) {
        return contactPhoneRepository.findByContact_ContactId(contactId);
    }

    // endregion

    // region Hobbies ----------------------- */

    @Transactional
    @Override
    public Hobby addNewHobby(HobbyDTO hobbyDto) {
        LOGGER.info("Adding new hobby with information: {}", hobbyDto);

        //Creates an instance of a Hobby
        Hobby hobby = new Hobby(hobbyDto.getHobbyTitle());
        return hobbyRepository.save(hobby);

    }

    @Transactional(rollbackFor = NotFoundException.class)
    @Override
    public Hobby updateHobbyTitle(HobbyDTO hobbyDto) throws NotFoundException {
        LOGGER.info("Updating hobby with information: {}", hobbyDto);

        Hobby found = hobbyRepository.findOne(hobbyDto.getHobbyId());

        //Update the hobby titles
        found.update(hobbyDto.getHobbyTitle());
        return found;
    }

    @Override
    public List<Hobby> findAllContacts() {
        return Lists.newArrayList(hobbyRepository.findAll());
    }

    @Transactional(value = "jpaTransactionManager", readOnly = true)
    @Override
    public Hobby findByHobbyTitle(String hobbyTitle) {
        return hobbyRepository.findByHobbyTitleIgnoreCase(hobbyTitle);
    }

    private void saveNewHobbiesToDatabase(ContactDTO added) {
        for (HobbyDTO hobbyDTO : added.getHobbies()) {
            Hobby hobby = hobbyRepository.findByHobbyTitleIgnoreCase(hobbyDTO.getHobbyTitle());
            if (hobby == null) {
                hobby = new Hobby(hobbyDTO.getHobbyTitle());
                hobbyRepository.save(hobby);
            }
        }
    }

    // endregion
}
