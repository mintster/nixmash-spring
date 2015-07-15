package com.nixmash.springdata.mvc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nixmash.springdata.jpa.common.SpringUtils;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.exceptions.ContactNotFoundException;
import com.nixmash.springdata.jpa.exceptions.UnknownResourceException;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@SessionAttributes("contact")
public class ContactController {

    private ContactService contactService;
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    protected static final String FEEDBACK_MESSAGE_KEY_CONTACT_ADDED = "feedback.message.contact.added";
    protected static final String FEEDBACK_MESSAGE_KEY_CONTACT_UPDATED = "feedback.message.contact.updated";
    protected static final String FEEDBACK_MESSAGE_KEY_CONTACT_DELETED = "feedback.message.contact.deleted";

    protected static final String FLASH_MESSAGE_KEY_ERROR = "errorMessage";
    protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    protected static final String CONTACT_VIEW = "view";
    protected static final String CONTACT_LIST_VIEW = "list";
    protected static final String CONTACT_FORM_VIEW = "contactform";
    protected static final String SEARCH_VIEW = "search";
    protected static final String HOME_VIEW = "home";

    protected static final String MODEL_ATTRIBUTE_CONTACT = "contact";
    protected static final String MODEL_ATTRIBUTE_CONTACTS = "contacts";
    protected static final String PARAMETER_CONTACT_ID = "id";

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Resource
    private MessageSource messageSource;

    // remember non-editable domain object values to send to service layer
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute(MODEL_ATTRIBUTE_CONTACTS)
    public List<Contact> allContacts() {
        return contactService.findAll();
    }

    @RequestMapping(value = {"{path:(?!webjars|static).*$}",
            "{path:(?!webjars|static).*$}/**"}, headers = "Accept=text/html")
    public void unknown() {
        throw new UnknownResourceException();
    }

    @RequestMapping(value = "/contact/json/{id}", method = GET)
    public
    @ResponseBody
    ContactDTO contactById(@PathVariable Long id) throws ContactNotFoundException {
        Contact contact = contactService.findContactById(id);
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        return SpringUtils.contactToContactDTO(contact);
    }



    @RequestMapping(value = "/contact/new", method = RequestMethod.GET)
    public String initAddContactForm(Model model) {
        Contact contact = new Contact();
        model.addAttribute(contact);
        return CONTACT_FORM_VIEW;
    }

    @RequestMapping(value = "/contact/new", method = RequestMethod.POST)
    public String addContact(@Valid Contact contact, BindingResult result,
                             SessionStatus status, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return CONTACT_FORM_VIEW;
        } else {
            ContactDTO contactDTO = SpringUtils.contactToContactDTO(contact);
            Contact added = contactService.add(contactDTO);
            logger.info("Added contact with information: {}", added);
            status.setComplete();

            addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_CONTACT_ADDED,
                    added.getFirstName(), added.getLastName());

            return "redirect:/contacts/";
        }
    }

    @RequestMapping(value = "/contact/{contactId}", method = GET)
    public String contactDisplayPage(@PathVariable("contactId") Long id, Model model)
            throws ContactNotFoundException {
        logger.info("Showing contact page for contact with id: {}", id);

        Contact found = contactService.findContactById(id);
        logger.info("Found contact: {}", found);

        model.addAttribute(MODEL_ATTRIBUTE_CONTACT, found);
        return CONTACT_VIEW;
    }

    @RequestMapping(value = "/contact/update/{contactId}", method = GET)
    public String contactEditPage(@PathVariable("contactId") Long id, Model model)
            throws ContactNotFoundException {
        logger.info("Showing contact update page for contact with id: {}", id);

        Contact found = contactService.getContactByIdWithDetail(id);
        logger.info("Found contact: {}", found);

        model.addAttribute(MODEL_ATTRIBUTE_CONTACT, found);
        return CONTACT_FORM_VIEW;
    }

    @RequestMapping(value = "/contact/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@Valid Contact contact, BindingResult result,
                                SessionStatus status, RedirectAttributes attributes)
            throws ContactNotFoundException {
        if (result.hasErrors()) {
            return CONTACT_FORM_VIEW;
        } else {
            ContactDTO updated = SpringUtils.contactToContactDTO(contact);
            updated.setUpdateChildren(false);
            this.contactService.update(updated);
            status.setComplete();

            attributes.addAttribute(PARAMETER_CONTACT_ID, updated.getContactId());
            addFeedbackMessage(attributes,
                    FEEDBACK_MESSAGE_KEY_CONTACT_UPDATED,
                    updated.getFirstName(), updated.getLastName());

            return "redirect:/contacts";
        }

    }

    @RequestMapping(value = "/contacts", method = GET)
    public String showContactsPage(Model model) {
        logger.info("Showing all contacts page");
        return CONTACT_LIST_VIEW;
    }

    @RequestMapping(value = "/", method = GET)
    public String home(Model model) {
        return HOME_VIEW;
    }

    @RequestMapping(value = "/search", method = GET)
    public String search(Model model) {
        model.addAttribute(MODEL_ATTRIBUTE_CONTACT, new ContactDTO());
        return SEARCH_VIEW;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String processFindForm(Contact contact, BindingResult result,
                                  Model model, HttpSession session) {
        Collection<Contact> results = null;

        if (StringUtils.isEmpty(contact.getLastName())) {
            // allow parameterless GET request to return all contacts
            return "redirect:/contacts/";
        } else {
            results = this.contactService.searchByLastName(contact.getLastName());
        }

        if (results.size() < 1) {
            // no contacts found
            result.rejectValue("lastName", "search.contact.notfound",
                    new Object[]{contact.getLastName()}, "not found");
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

    /**
     * Adds a flash feedback message.
     * @param model The model which contains the message.
     * @param code  The code used to fetch the localized message.
     * @param params    The params of the message.
     */
    private void addFeedbackMessage(RedirectAttributes model, String code, Object... params) {
        logger.info("Adding feedback message with code: {} and params: {}", code, params);
        String localizedFeedbackMessage = getMessage(code, params);
        logger.info("Localized message is: {}", localizedFeedbackMessage);
        model.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }

    /**
     * Gets a message from the message source.
     * @param code  The message code.
     * @param params    The params of the message.
     * @return  The localized message.
     */
    private String getMessage(String code, Object... params) {
        Locale current = LocaleContextHolder.getLocale();
        logger.info("Current locale is {}", current);
        return messageSource.getMessage(code, params, current);
    }

}
