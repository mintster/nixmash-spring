package io.hibernate;

import io.hibernate.model.Contact;
import io.hibernate.service.ContactService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class SpringHibernate {

    private static SpringProperties springProperties;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringConfiguration.class);
        ctx.refresh();

        ContactService contactService = (ContactService) ctx.getBean("contactService");

        springProperties = ctx.getBean(SpringProperties.class);

//        propertiesDemo();

        hibernateDemo(contactService);


    }

//        region v0.0.2 propertiesDemo()
    private static void propertiesDemo() {
        SpringUtils.printProperty(
                "springProperties.getToken() in another class",
                springProperties.getToken());
    }

//    endregion

    private static void hibernateDemo(ContactService contactService) {

        // region List Single Contact

        Contact contact = contactService.getContact(1l);
        System.out.println();
        System.out.println("CONTACT WITH ID 1 --------------------------------------");
        System.out.println();
        System.out.println(contact);
        System.out.println();

        // endregion

        // region List All Contacts Without Detail

        List<Contact> contacts = contactService.getContacts();
        listContacts(contacts);

        // endregion

        // region Add Contact

/*
        Contact contact = new Contact();
        contact.setFirstName("Michael");
        contact.setLastName("Jackson");
        contact.setBirthDate(new Date());
        ContactTelDetail contactTelDetail =
                new ContactTelDetail("Home", "1111111111");
        contact.addContactTelDetail(contactTelDetail);
        contactTelDetail = new ContactTelDetail("Mobile", "2222222222");
        contact.addContactTelDetail(contactTelDetail);
        contactDao.save(contact);
*/

        // endregion

        // region List All Contacts with Details

        contacts = contactService.getContactsWithDetail();
        listContactsWithDetail(contacts);

        // endregion
    }

    private static void listContacts(List<Contact> contacts) {
        System.out.println("LISTING CONTACTS WITHOUT DETAILS -----------------------------");
        System.out.println();
        contacts.forEach(System.out::println);
        System.out.println();
    }

    private static void listContactsWithDetail(List<Contact> contacts) {
        System.out.println("LISTING CONTACTS WITH DETAILS ---------------------------------");
        System.out.println();
        for (Contact contact : contacts) {
            System.out.println(contact);
            if (contact.getContactTelDetails() != null) {
                contact.getContactTelDetails().forEach(System.out::println);
            }
            if (contact.getHobbies() != null) {
                contact.getHobbies().forEach(System.out::println);
            }
            System.out.println();
        }
    }
}
