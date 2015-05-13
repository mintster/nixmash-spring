package com.nixmash.springdata.jpa.common;

import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.model.Contact;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 4/15/15
 * Time: 12:06 PM
 */
public class SpringUtils {

    // region Properties

    public static void printProperty(String header, String property) {
        System.out.println("\r\n" + header + " ------------------------------ ");
        System.out.println(property);
    }

    // endregion

    // region List Contacts

    public static void listContact(String header, Contact contact) {
        System.out.println("\r\n" + header + " ------------------------------ ");
        System.out.println();
        System.out.println(contact);
        System.out.println();
    }

    public static void listContacts(String header, List<Contact> contacts) {
        System.out.println("\r\n" + header + " ------------------------------ ");
        System.out.println();
        contacts.forEach(System.out::println);
        System.out.println();
    }

    public static void listContactWithDetail(Contact contact) {
        System.out.println("SINGLE CONTACT WITH DETAILS ---------------------------------");
        System.out.println();
            System.out.println(contact);
            if (contact.getContactPhones() != null) {
                contact.getContactPhones().forEach(System.out::println);
            }
            if (contact.getHobbies() != null) {
                contact.getHobbies().forEach(System.out::println);
            }
            System.out.println();
    }

    public static void listContactsWithDetail(List<Contact> contacts) {
        System.out.println("LISTING ENTITIES WITH DETAILS ---------------------------------");
        System.out.println();
        for (Contact contact : contacts) {
            System.out.println(contact);
            if (contact.getContactPhones() != null) {
                contact.getContactPhones().forEach(System.out::println);
            }
            if (contact.getHobbies() != null) {
                contact.getHobbies().forEach(System.out::println);
            }
            System.out.println();
        }
    }

    // endregion

    // region Update Contacts, Phones and Hobbies

    public static ContactDTO createContactDTO(Contact model) {
        ContactDTO dto = new ContactDTO();

        dto.setContactId(model.getContactId());
        dto.setFirstName(model.getFirstName());
        dto.setBirthDate(model.getBirthDate());
        dto.setLastName("Goff");
        dto.setEmail(model.getEmail());
        if (model.getContactPhones() != null) {
            model.getContactPhones().forEach(System.out::println);
//            Set<ContactPhoneDTO> results = model.getContactPhones().stream().map(ContactPhoneDTO::new).collect(Collectors.toSet());
//            dto.setContactPhones(results);

            System.out.println(dto.toString());
        }
        return dto;
    }
    // endregion
}
