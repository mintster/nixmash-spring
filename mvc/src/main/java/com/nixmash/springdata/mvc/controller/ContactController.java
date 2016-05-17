package com.nixmash.springdata.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nixmash.springdata.jpa.utils.ContactUtils;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.exceptions.ContactNotFoundException;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactPhone;
import com.nixmash.springdata.jpa.model.Hobby;
import com.nixmash.springdata.jpa.model.validators.ContactFormValidator;
import com.nixmash.springdata.jpa.service.ContactService;
import com.nixmash.springdata.mvc.components.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@SessionAttributes("contact")
public class ContactController {

	private ContactService contactService;
	private ContactFormValidator contactFormValidator;
	private WebUI webUI;

	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

	protected static final String FEEDBACK_MESSAGE_KEY_CONTACT_ADDED = "feedback.message.contact.added";
	protected static final String FEEDBACK_MESSAGE_KEY_CONTACT_UPDATED = "feedback.message.contact.updated";

	protected static final String CONTACT_VIEW = "contacts/view";
	protected static final String CONTACT_LIST_VIEW = "contacts/list";
	protected static final String CONTACT_FORM_VIEW = "contacts/contactform";
	protected static final String SEARCH_VIEW = "contacts/search";
	protected static final String CONTACT_OF_THE_DAY_FRAGMENT = "fragments/js :: contactOfTheDay";

	protected static final String MODEL_ATTRIBUTE_CONTACT = "contact";
	protected static final String MODEL_ATTRIBUTE_CONTACTS = "contacts";
	protected static final String MODEL_ATTRIBUTE_HOBBIES = "hobbies";
	protected static final String PARAMETER_CONTACT_ID = "id";

	@Autowired
	public ContactController(ContactService contactService, ContactFormValidator contactFormValidator, WebUI webUI) {
		this.contactService = contactService;
		this.contactFormValidator = contactFormValidator;
		this.webUI = webUI;
	}

	@InitBinder("contact")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(contactFormValidator);
		binder.setDisallowedFields("id");
	}

	@ModelAttribute(MODEL_ATTRIBUTE_CONTACTS)
	public List<Contact> allContacts() {
		return contactService.findAll();
	}

	@RequestMapping(value = "/json/contact/{id}", method = GET)
	public @ResponseBody ContactDTO contactById(@PathVariable Long id) throws ContactNotFoundException {
		Contact contact = contactService.findContactById(id);
		ObjectMapper jacksonMapper = new ObjectMapper();
		jacksonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

		return ContactUtils.contactToContactDTO(contact);
	}

	@RequestMapping(value = "/json/cod", method = GET)
	public String contactOfTheDay(final Model model) throws ContactNotFoundException {
		Contact contact = contactService.findContactById(ContactUtils.randomContactId());
		model.addAttribute("cod", contact);
		return CONTACT_OF_THE_DAY_FRAGMENT;

	}

	@RequestMapping(value = "/json/secret", method = GET)
	public @ResponseBody Map<String, String> secretMessage() {
		Map<String, String> map = new HashMap<>();
		map.put("message", webUI.getMessage("js.secret.message"));
		return map;

	}

	@RequestMapping(value = "/contact/new", method = RequestMethod.GET)
	public String initAddContactForm(Model model) {
		Contact contact = new Contact();
		model.addAttribute(contact);
		return CONTACT_FORM_VIEW;
	}

	@RequestMapping(value = "/contact/new", method = RequestMethod.POST)
	public String addContact(@Valid Contact contact, BindingResult result, SessionStatus status,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return CONTACT_FORM_VIEW;
		} else {
			ContactDTO contactDTO = ContactUtils.contactToContactDTO(contact);
			Contact added = contactService.add(contactDTO);
			logger.info("Added contact with information: {}", added);
			status.setComplete();

			webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_CONTACT_ADDED, added.getFirstName(),
					added.getLastName());

			return "redirect:/contacts";
		}
	}

	@RequestMapping(value = "/contact/{contactId}", method = GET)
	public String contactDisplayPage(@PathVariable("contactId") Long id, Model model) throws ContactNotFoundException {
		logger.info("Showing contact page for contact with id: {}", id);

		Contact found = contactService.findContactById(id);
		logger.info("Found contact: {}", found);

		model.addAttribute(MODEL_ATTRIBUTE_CONTACT, found);
		return CONTACT_VIEW;
	}

	@RequestMapping(value = "/contact/update/{contactId}", method = GET)
	public String contactEditPage(@PathVariable("contactId") Long id, Model model) throws ContactNotFoundException {
		logger.info("Showing contact update page for contact with id: {}", id);

		Contact found = contactService.getContactByIdWithDetail(id);
		logger.info("Found contact: {}", found);

		List<Hobby> hobbies = contactService.findAllHobbies();
		model.addAttribute(MODEL_ATTRIBUTE_CONTACT, found);
		model.addAttribute(MODEL_ATTRIBUTE_HOBBIES, hobbies);
		return CONTACT_FORM_VIEW;
	}

	@RequestMapping(value = "/contact/update/{contactId}", params = { "addContactPhone" }, method = RequestMethod.POST)
	public String addRow(final Contact contact) {
		ContactPhone contactPhone = ContactPhone.getBuilder(contact, null, null).build();
		contactPhone.setContactPhoneId(ContactUtils.randomNegativeId());
		contact.getContactPhones().add(contactPhone);
		return CONTACT_FORM_VIEW;
	}

	@RequestMapping(value = "/contact/update/{contactId}", params = {
			"removeContactPhone" }, method = RequestMethod.POST)
	public String removeRow(final Contact contact, final HttpServletRequest req) throws ContactNotFoundException {
		final Long contactPhoneId = Long.valueOf(req.getParameter("removeContactPhone"));

		for (ContactPhone contactPhone : contact.getContactPhones()) {
			if (contactPhone.getContactPhoneId().equals(contactPhoneId)) {
				contact.getContactPhones().remove(contactPhone);
				break;
			}
		}

		if (contactPhoneId > 0)
			contactService.deleteContactPhoneById(contactPhoneId);

		return CONTACT_FORM_VIEW;
	}

	@RequestMapping(value = "/contact/update/{contactId}", method = RequestMethod.POST)
	public String updateContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
			RedirectAttributes attributes, Model model) throws ContactNotFoundException {
		if (result.hasErrors()) {
			if (contact.getHobbies() == null) {
				List<Hobby> hobbies = contactService.findAllHobbies();
				model.addAttribute(MODEL_ATTRIBUTE_HOBBIES, hobbies);
			}
			return CONTACT_FORM_VIEW;
		} else {

			ContactDTO contactDTO = ContactUtils.contactToContactDTO(contact);
			contactDTO.setUpdateChildren(true);
			this.contactService.update(contactDTO);

			attributes.addAttribute(PARAMETER_CONTACT_ID, contactDTO.getContactId());
			webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_CONTACT_UPDATED, contactDTO.getFirstName(),
					contactDTO.getLastName());

			return "redirect:/contacts";
		}

	}

	@RequestMapping(value = "/contacts", method = GET)
	public String showContactsPage(Model model) {
		logger.info("Showing all contacts page");
		return CONTACT_LIST_VIEW;
	}

	@RequestMapping(value = "/contacts/search", method = GET)
	public String search(Model model, HttpServletRequest request) {
		model.addAttribute(MODEL_ATTRIBUTE_CONTACT, new Contact());
		return SEARCH_VIEW;
	}

	@RequestMapping(value = "/contacts/list", method = RequestMethod.GET)
	public String processFindForm(Contact contact, BindingResult result, Model model, HttpSession session) {
		Collection<Contact> results = null;

		if (StringUtils.isEmpty(contact.getLastName())) {
			// allow parameterless GET request to return all contacts
			return "redirect:/contacts/";
		} else {
			results = this.contactService.searchByLastName(contact.getLastName());
		}

		if (results.size() < 1) {
			// no contacts found
			result.rejectValue("lastName", "contact.search.notfound", new Object[] { contact.getLastName() },
					"not found");
			return SEARCH_VIEW;
		}

		session.setAttribute("searchLastName", contact.getLastName());

		if (results.size() > 1) {
			// multiple contacts found
			model.addAttribute(MODEL_ATTRIBUTE_CONTACTS, results);
			return CONTACT_LIST_VIEW;
		} else {
			// 1 contact found
			contact = results.iterator().next();
			return "redirect:/contact/" + contact.getContactId();
		}
	}

}