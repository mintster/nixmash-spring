package com.nixmash.springdata.jpa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nixmash.springdata.jpa.TestUtil;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class ContactTests {

	private static final String EMAIL = "foo.bar@bar.com";
	private static final String FIRST_NAME = "Foo";
	private static final String LAST_NAME = "Bar";
	private static final Date BIRTH_DATE = TestUtil.date(1969, 6, 9);

	@Test
	public void buildWithMandatoryValues() {
		Contact build = Contact.getBuilder(FIRST_NAME, LAST_NAME, EMAIL).build();

		assertNull(build.getContactId());

		assertEquals(FIRST_NAME, build.getFirstName());
		assertEquals(LAST_NAME, build.getLastName());
		assertEquals(EMAIL, build.getEmail());
		assertNull(build.getBirthDate());
	}

	@Test
	public void buildWithMandatoryValuesAndBirthDate() {
		Contact build = Contact.getBuilder(FIRST_NAME, LAST_NAME, EMAIL).birthDate(BIRTH_DATE).build();

		assertNull(build.getContactId());

		assertEquals(FIRST_NAME, build.getFirstName());
		assertEquals(LAST_NAME, build.getLastName());
		assertEquals(EMAIL, build.getEmail());
		assertEquals(BIRTH_DATE, build.getBirthDate());

	}

}
