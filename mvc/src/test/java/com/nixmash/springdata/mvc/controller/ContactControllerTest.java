package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.service.ContactService;
import com.nixmash.springdata.mvc.MvcTestUtil;
import com.nixmash.springdata.mvc.config.WebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(classes = {WebConfig.class, ApplicationConfig.class})
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
@WebAppConfiguration
public class ContactControllerTest {

    private ContactService mockService;
    private ContactController controller;
    private MockMvc mockMvc;
    private Contact contact;
    private List<Contact> allContacts;

    @Autowired
    private ContactService contactService;

    @Before
    public void setUp() {

        mockService = mock(ContactService.class);
        controller = new ContactController(mockService);
        mockMvc = standaloneSetup(controller).build();

        // Contact is H2 Contact ID #1 "Summer Glass"
        contact = contactService.findContactById(1L);
        when(mockService.findContactById(100L)).thenReturn(contact);

        allContacts = contactService.findAll();
        when(mockService.findAll()).thenReturn(allContacts);

    }

    @Test
    public void homePageTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("home"));
    }


    @Test
    public void getContactByIdJsonTest() throws Exception {

        mockMvc.perform(get("/contact/json/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MvcTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.contactId", is(1)))
                .andExpect(jsonPath("$.lastName", is("Glass")));
//                .andDo(print())
//                .andReturn();

    }

    @Test
    public void getContactByIdTest() throws Exception {

        mockMvc.perform(get("/contact/100").contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("view"))
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attribute("contact", contact));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getContactsTest() throws Exception {

        Model model = new BindingAwareModelMap();
        String view = controller.home(model);

        MvcResult result = mockMvc.perform(get("/contacts"))
                .andExpect(view().name("list"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attribute("contacts",
                        hasItems(allContacts.toArray())))
                .andReturn();

        List<ContactDTO> allContacts = (List<ContactDTO>) result.getModelAndView().getModel().get("contacts");
        assertEquals("there should be 10 contacts", allContacts.size(), 10);

        verify(mockService, times(1)).findAll();
        verifyNoMoreInteractions(mockService);

        assertEquals(ContactController.HOME_VIEW, view);
    }

    @Test
    public void searchContactsTest() throws Exception {

        mockMvc = standaloneSetup(new ContactController(contactService))
                .setSingleView(
                        new InternalResourceView("/WEB-INF/views/list.html"))
                .build();

        // TEST SINGLE CONTACT RETRIEVED

        mockMvc.perform(get("/list").param("lastName", "Glass"))
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:/contact/1"));

        // TEST MULTIPLE CONTACTS RETRIEVED

        mockMvc.perform(get("/list").param("lastName", "Rob"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("contacts"))
                .andExpect(view().name("list"));

        // TEST NO CONTACTS RETRIEVED

        mockMvc.perform(get("/list").param("lastName", "Bubba"))
                .andExpect(status().isOk())
                .andExpect(model()
                        .attributeHasFieldErrorCode("contact",
                                "lastName",
                                "search.contact.notfound"))
                .andExpect(view().name("search"));


        // TEST EMPTY FORM - ALL CONTACTS RETRIEVED

        mockMvc.perform(get("/list").param("lastName", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("contacts"))
                .andExpect(view().name("redirect:/contacts/"))
                .andExpect(model().attribute("contacts",
                        hasItems(allContacts.toArray())));

    }

}
