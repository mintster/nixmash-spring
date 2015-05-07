package com.nixmash.springdata.jpa.config;

import com.nixmash.springdata.jpa.model.ContactEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: daveburke
 * Date: 4/15/15
 * Time: 12:06 PM
 */
public class SpringUtils {

    public static void printProperty(String header, String property) {
        System.out.println("\r\n" + header + " ------------------------------ ");
        System.out.println(property);
    }


    public static void listContactEntities(String header, List<ContactEntity> contacts) {

        System.out.println("\r\n" + header + " ------------------------------ ");
        System.out.println();
        contacts.forEach(System.out::println);
        System.out.println();
    }

    public static void listContactEntity(String header, ContactEntity contact) {
        System.out.println("\r\n" + header + " ------------------------------ ");
        System.out.println();
        System.out.println(contact);
        System.out.println();
    }


    public static void listContactEntitiesWithDetail(List<ContactEntity> contacts) {
        System.out.println("LISTING ENTITIES WITH DETAILS ---------------------------------");
        System.out.println();
        for (ContactEntity contact : contacts) {
            System.out.println(contact);
            if (contact.getContactTelDetailEntities() != null) {
                contact.getContactTelDetailEntities().forEach(System.out::println);
            }
            if (contact.getHobbyEntities() != null) {
                contact.getHobbyEntities().forEach(System.out::println);
            }
            System.out.println();
        }
    }

}
