package io.hibernate;

import io.hibernate.dao.Contact;
import io.hibernate.dao.ContactDao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class SpringHibernateSample {

    private static PropertyClass propertyClass;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        ContactDao contactDao = ctx.getBean(ContactDao.class);
        propertyClass = ctx.getBean(PropertyClass.class);

        propertiesDemo();
        hibernateDemo(contactDao);


    }

    private static void propertiesDemo() {
        SpringPropertiesUtil.printProperty(
                "propertyClass.getToken() in another class",
                propertyClass.getToken());
    }

    private static void hibernateDemo(ContactDao contactDao) {
        // region List Single Contact

        Contact contact = contactDao.findById(1l);
        System.out.println();
        System.out.println("CONTACT WITH ID 1 --------------------------------------");
        System.out.println();
        System.out.println(contact);
        System.out.println();

        // endregion

        // region List All Contacts Without Detail

        List<Contact> contacts = contactDao.findAll();
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

        contacts = contactDao.findAllWithDetail();
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
