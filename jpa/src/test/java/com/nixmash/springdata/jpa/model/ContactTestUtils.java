package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.TestUtil;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.dto.ContactPhoneDTO;
import com.nixmash.springdata.jpa.dto.HobbyDTO;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Petri Kainulainen
 */
public class ContactTestUtils {

    private static final String CHARACTER = "a";

    public static final Long CONTACT_ID = 100L;
    public static final String INVALID_EMAIL = "foo.bar";

    private static final String EMAIL = "foo.bar@bar.com";
    private static final String FIRST_NAME = "Foo";
    private static final String LAST_NAME = "Bar";
    private static final Date BIRTH_DATE = TestUtil.date(1969, 6, 9);
    private static final String ADMIN_USERNAME = "admin";
    private static final ZonedDateTime ZONED_DATE_TIME = TestUtil.currentZonedDateTime();

    public static final String HOBBY_TITLE = "Quilting";

    private static final Set<ContactPhoneDTO> CONTACT_PHONE_DTOS =
            createContactPhoneDTOs();
    public static final HobbyDTO JOUSTING_HOBBY_DTO = createHobbyDTO();
    public static final ContactPhoneDTO HOME_CONTACT_PHONE_DTO = createContactPhoneDTO();

    private static final Set<HobbyDTO> HOBBY_DTOS = createHobbyDTOs();

    @SuppressWarnings("serial")
	private static Set<HobbyDTO> createHobbyDTOs() {
        return new HashSet<HobbyDTO>() {{
            add(new HobbyDTO("Jogging"));
            add(new HobbyDTO("Movies"));
            add(new HobbyDTO("Hip-hopping"));
        }};
    }

    // region ContactDTO ---------------------------------------- */

    private static HobbyDTO createHobbyDTO() {
        return new HobbyDTO("Jousting");
    }

    private static ContactPhoneDTO createContactPhoneDTO() {
        return new ContactPhoneDTO("Home", "717-244-4444");
    }

    @SuppressWarnings("serial")
	private static Set<ContactPhoneDTO> createContactPhoneDTOs() {
        return new HashSet<ContactPhoneDTO>() {{
            add(new ContactPhoneDTO("Mobile", "717-244-2222"));
            add(new ContactPhoneDTO("Business", "717-244-3333"));
        }};
    }

    public static ContactDTO newContactDTO(boolean addChildren) {
        ContactDTO dto = new ContactDTO();
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setBirthDate(BIRTH_DATE);
        dto.setEmail(EMAIL);
        dto.setCreatedByUser(ADMIN_USERNAME);
        dto.setCreationTime(ZONED_DATE_TIME);
        dto.setModifiedByUser(ADMIN_USERNAME);
        dto.setModificationTime(ZONED_DATE_TIME);
        if (addChildren) {
            dto.setContactPhones(CONTACT_PHONE_DTOS);
            dto.setHobbies(HOBBY_DTOS);
        }
        return dto;
    }

    public static ContactDTO newContactDTO() {
        return newContactDTO(true);
    }

    public static Contact newContact(Long contactId) {
        Contact contact = new Contact();
        contact.setContactId(contactId);
        contact.setFirstName(FIRST_NAME);
        contact.setLastName(LAST_NAME);
        contact.setBirthDate(BIRTH_DATE);
        contact.setEmail(EMAIL);
        contact.setCreatedByUser(ADMIN_USERNAME);
        contact.setCreationTime(ZONED_DATE_TIME);
        contact.setModifiedByUser(ADMIN_USERNAME);
        contact.setModificationTime(ZONED_DATE_TIME);
        return contact;
    }

    public static Contact newContact() {
        return newContact(CONTACT_ID);
    }

    public static ContactDTO contactToContactDTO(Contact contact) {
        ContactDTO dto = new ContactDTO();

        dto.setContactId(contact.getContactId());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setBirthDate(contact.getBirthDate());
        dto.setEmail(contact.getEmail());
        dto.setCreatedByUser(ADMIN_USERNAME);
        dto.setCreationTime(ZONED_DATE_TIME);
        dto.setModifiedByUser(ADMIN_USERNAME);
        dto.setModificationTime(ZONED_DATE_TIME);
        if (contact.getContactPhones() != null) {
            dto.setContactPhones(contact.getContactPhones()
                    .stream()
                    .map(ContactPhoneDTO::new)
                    .collect(Collectors.toSet()));
        }
        if (contact.getHobbies() != null) {
            dto.setHobbies(contact.getHobbies()
                    .stream()
                    .map(HobbyDTO::new)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    public static ContactDTO addHobbyToContactDTO(ContactDTO contactDTO) {
        contactDTO.getHobbies().add(JOUSTING_HOBBY_DTO);
        return contactDTO;
    }

    public static ContactDTO addContactPhoneToContactDTO(ContactDTO contactDTO) {
        contactDTO.getContactPhones().add(HOME_CONTACT_PHONE_DTO);
        return contactDTO;
    }

    // endregion

    // region Hobbies ------------------------------------- */

    public static HobbyDTO newHobbyDTO() {
        HobbyDTO hobbyDTO = new HobbyDTO();
        hobbyDTO.setHobbyTitle(HOBBY_TITLE);
        return hobbyDTO;
    }


    // endregion
}
