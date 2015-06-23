package com.nixmash.springdata.mvc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nixmash.springdata.jpa.common.SpringUtils;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.service.ContactService;
import com.nixmash.springdata.jpa.service.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ContactController {

    private ContactService contactService;
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    protected static final String ADD_CONTACT_VIEW = "add";
    protected static final String CONTACT_VIEW = "view";
    protected static final String CONTACT_LIST_VIEW = "list";
    protected static final String UPDATE_CONTACT_VIEW = "update";
    protected static final String SEARCH_VIEW = "search";
    protected static final String HOME_VIEW = "home";

    protected static final String MODEL_ATTRIBUTE_CONTACT = "contact";
    protected static final String MODEL_ATTRIBUTE_CONTACTS = "contacts";

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


    @ModelAttribute(MODEL_ATTRIBUTE_CONTACTS)
    public List<Contact> allContacts() {
        return contactService.findAll();
    }

    @RequestMapping(value = "/contact/json/{id}", method = GET)
    public @ResponseBody
    ContactDTO contactById(@PathVariable Long id) {
        Contact contact = contactService.findContactById(id);
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        return SpringUtils.contactToContactDTO(contact);
    }


    @RequestMapping(value = "/contact/{id}", method = GET)
    public String showContactPage(@PathVariable("id") Long id, Model model) throws NotFoundException {
        logger.info("Showing contact page for contact with id: {}", id);

        Contact found = contactService.findContactById(id);
        logger.info("Found contact: {}", found);

        model.addAttribute(MODEL_ATTRIBUTE_CONTACT, found);
        return CONTACT_VIEW;
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
                    new Object[] {contact.getLastName()}, "not found");
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
