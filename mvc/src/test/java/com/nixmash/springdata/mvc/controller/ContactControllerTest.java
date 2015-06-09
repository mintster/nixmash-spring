package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.ContactDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.Contact;
import com.nixmash.springdata.jpa.model.ContactTestUtils;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
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
    private MockMvc mockMvc;

    @Autowired
    private ContactService contactService;

    @Before
    public void setUp() {
        mockService = mock(ContactService.class);
        ContactController controller = new ContactController(mockService);
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void getContactByIdTest() throws Exception {


        // Contact is H2 Contact ID #1 "Summer Glass"
        Contact contact = contactService.findContactById(1L);
        when(mockService.findContactById(100L)).thenReturn(contact);

        ContactDTO contactDTO = ContactTestUtils.contactToContactDTO(contact);

        mockMvc.perform(get("/contact/100").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MvcTestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.contactId", is(1)))
                .andExpect(jsonPath("$.lastName", is("Glass")));
//                .andDo(print())
//                .andReturn();

        verify(mockService, times(1)).findContactById(100L);
        verifyNoMoreInteractions(mockService);


    }

}
