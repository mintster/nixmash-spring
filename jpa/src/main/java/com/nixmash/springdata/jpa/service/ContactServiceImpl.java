package com.nixmash.springdata.jpa.service;

import com.google.common.collect.Lists;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.dto.ContactPhoneDTO;
import com.nixmash.springdata.jpa.dto.HobbyDTO;
import com.nixmash.springdata.jpa.exceptions.ContactNotFoundException;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactPhone;
import com.nixmash.springdata.jpa.model.Hobby;
import com.nixmash.springdata.jpa.repository.ContactPhoneRepository;
import com.nixmash.springdata.jpa.repository.ContactRepository;
import com.nixmash.springdata.jpa.repository.HobbyRepository;
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
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

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

    @Transactional(readOnly = true)
    public List<Contact> findAll() {
        return Lists.newArrayList(contactRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<Contact> findByFirstName(String firstName) {
        return contactRepository.findByFirstName(firstName);
    }

    @Transactional(readOnly = true)
    public List<Contact> findByFirstNameAndLastName(
            String firstName, String lastName) {
        return contactRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Transactional(readOnly = true)
    public List<Contact> searchByLastName(String lastName) {
        return contactRepository.findByLastNameIgnoreCaseContains(lastName);
    }

    @Transactional(readOnly = true)
    public List<Contact> getContactsWithDetail() {
        return contactRepository.findAllWithDetail();
    }

    @Transactional(readOnly = true)
    public Contact getContactByIdWithDetail(Long ID) {
        return contactRepository.findByContactIdWithDetail(ID);
    }

    @Transactional(readOnly = true)
    public Contact findContactById(Long ID) throws ContactNotFoundException {

        logger.info("Finding contact by id: {}", ID);

        Contact found = contactRepository.findOne(ID);

        if (found == null) {
            logger.info("No contact found with id: {}", ID);
            throw new ContactNotFoundException("No contact found with id: " + ID);
        }

        return found;
    }

    public Contact getContactByEmail(String email) {
        return contactRepository.findByEmail(email);
    }


    @Transactional(rollbackFor = ContactNotFoundException.class)
    @Override
    public Contact deleteById(Long id) throws ContactNotFoundException {
        logger.info("Deleting contact by id: {}", id);

        Contact deleted = findContactById(id);
        contactRepository.delete(deleted);

        logger.info("Deleted contact: {}", deleted);
        return deleted;
    }

    @Transactional(rollbackFor = ContactNotFoundException.class)
    @Override
    public Contact update(ContactDTO contactDto) throws ContactNotFoundException {
        logger.info("Updating contact with information: {}", contactDto);

        Contact found = findContactById(contactDto.getContactId());

        // Update the contact information
        found.update(contactDto.getFirstName(), contactDto.getLastName(), contactDto.getEmail(), contactDto.getBirthDate());
        // Update the contact phone if updateChildren(true)

        if (contactDto.isUpdateChildren()) {

            if (contactDto.getContactPhones() != null) {
                for (ContactPhoneDTO contactPhoneDTO : contactDto.getContactPhones()) {
                    ContactPhone contactPhone =
                            contactPhoneRepository.findByContactPhoneId(contactPhoneDTO.getContactPhoneId());
                    if (contactPhone != null) {
                        contactPhone.update(contactPhoneDTO.getPhoneType(), contactPhoneDTO.getPhoneNumber());
                    } else {
                        contactPhone = saveContactPhone(found, contactPhoneDTO);
                        found.getContactPhones().add(contactPhone);
                    }
                }
            }

            if (contactDto.getHobbies() != null) {
                saveNewHobbiesToDatabase(contactDto);
            }

            if (contactDto.getHobbies() != null) {
                found.getHobbies().clear();
                for (HobbyDTO hobbyDTO : contactDto.getHobbies()) {
                    Hobby hobby = hobbyRepository.findByHobbyTitleIgnoreCase(hobbyDTO.getHobbyTitle());

                    if (!found.getHobbies().contains(hobby))
                        found.getHobbies().add(hobby);
                }
            }
        }
        return found;
    }

    @Transactional
    @Override
    public Contact add(ContactDTO contactDto) {
        logger.info("Adding new contact with information: {}", contactDto);

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


    @Override
    public List<Hobby> findAllHobbies() {
        return Lists.newArrayList(hobbyRepository.findAll());
    }

    // endregion

    // region Contact Phones ---------------------- */

    @Override
    public ContactPhone deleteContactPhoneById(Long contactPhoneId) throws ContactNotFoundException {

        ContactPhone contactPhone = contactPhoneRepository.findOne(contactPhoneId);
        if (contactPhone != null) {
            logger.info("Removing contact phone with information: {}", contactPhone);
            contactPhoneRepository.delete(contactPhone);
        }

        return contactPhone;
    }

    @Transactional(readOnly = true)
    public List<ContactPhone> findContactPhonesByContactId(Long contactId) {
        return contactPhoneRepository.findByContact_ContactId(contactId);
    }

    @Override
    @Transactional(rollbackFor = ContactNotFoundException.class)
    public ContactPhone addContactPhone(ContactPhoneDTO contactPhoneDTO) {
        Contact contact = contactRepository.findOne(contactPhoneDTO.getContactId());
        ContactPhone contactPhone = ContactPhone.getBuilder(contact,
                contactPhoneDTO.getPhoneType(),
                contactPhoneDTO.getPhoneNumber())
                .build();

        return contactPhoneRepository.save(contactPhone);
    }

    @Override
    public ContactPhone findContactPhoneById(Long contactPhoneID) {
        return contactPhoneRepository.findByContactPhoneId(contactPhoneID);
    }

    // endregion

    // region Hobbies ----------------------- */

    @Transactional
    @Override
    public Hobby addHobby(HobbyDTO hobbyDto) {
        logger.info("Adding new hobby with information: {}", hobbyDto);

        Hobby hobby = hobbyRepository.findByHobbyTitleIgnoreCase(hobbyDto.getHobbyTitle());
        if (hobby == null) {
            hobby = new Hobby(hobbyDto.getHobbyTitle());
            hobby = hobbyRepository.save(hobby);
        }
        return hobby;

    }

    @Transactional(rollbackFor = ContactNotFoundException.class)
    @Override
    public Hobby updateHobbyTitle(HobbyDTO hobbyDto) throws ContactNotFoundException {
        logger.info("Updating hobby with information: {}", hobbyDto);

        Hobby found = hobbyRepository.findOne(hobbyDto.getHobbyId());

        //Update the hobby titles
        found.update(hobbyDto.getHobbyTitle());
        return found;
    }

    @Transactional(readOnly = true)
    @Override
    public Hobby findByHobbyTitle(String hobbyTitle) {
        return hobbyRepository.findByHobbyTitleIgnoreCase(hobbyTitle);
    }

    @Transactional(rollbackFor = ContactNotFoundException.class)
    @Override
    public Contact removeHobby(ContactDTO contactDto, Long hobbyId) throws ContactNotFoundException {
        logger.info("Removing contact hobby with information: {}", contactDto);

        Contact found = findContactById(contactDto.getContactId());
        Hobby hobby = hobbyRepository.findOne(hobbyId);

        if (found.getHobbies().contains(hobby))
            found.getHobbies().remove(hobby);

        return found;
    }


    // endregion

    // region Private Hobby and Contact Phone Methods


    @Transactional
    private ContactPhone saveContactPhone(Contact contact, ContactPhoneDTO contactPhoneDTO) {
        ContactPhone contactPhone = ContactPhone.getBuilder(contact,
                contactPhoneDTO.getPhoneType(),
                contactPhoneDTO.getPhoneNumber())
                .build();

        return contactPhoneRepository.save(contactPhone);
    }

    @Transactional
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
