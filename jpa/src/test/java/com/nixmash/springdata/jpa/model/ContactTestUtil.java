package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.TestUtil;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.dto.ContactPhoneDTO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Petri Kainulainen
 */
public class ContactTestUtil {

    private static final String CHARACTER = "a";

    private static final Long CONTACT_ID = 100L;
    private static final String EMAIL = "foo.bar@bar.com";
    private static final String FIRST_NAME = "Foo";
    private static final String LAST_NAME = "Bar";
    private static final Date BIRTH_DATE = TestUtil.date(1969, 6, 9);

    private static final Set<ContactPhoneDTO> CONTACT_PHONE_DTOS =
            createContactPhoneDTOs();

    private static Set<ContactPhoneDTO> createContactPhoneDTOs() {

        return new HashSet<ContactPhoneDTO>() {{
            add(new ContactPhoneDTO("Mobile", "717-244-2222"));
            add(new ContactPhoneDTO("Business", "717-244-3333"));
        }};
    }

    public static ContactDTO newContactDTO() {
        ContactDTO dto = new ContactDTO();
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setBirthDate(BIRTH_DATE);
        dto.setEmail(EMAIL);
        dto.setContactPhones(CONTACT_PHONE_DTOS);
        return dto;
    }

    public static ContactDTO contactToContactDTO(Contact contact) {
        ContactDTO dto = new ContactDTO();

        dto.setContactId(contact.getContactId());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setBirthDate(contact.getBirthDate());
        dto.setEmail(contact.getEmail());
        if (contact.getContactPhones() != null) {
            dto.setContactPhones(contact.getContactPhones()
                    .stream()
                    .map(ContactPhoneDTO::new)
                    .collect(Collectors.toSet()));
            }

        return dto;
    }

}
