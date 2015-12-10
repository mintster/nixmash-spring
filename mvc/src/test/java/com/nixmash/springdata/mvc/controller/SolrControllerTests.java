package com.nixmash.springdata.mvc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.solr.service.ProductService;


@RunWith(SpringJUnit4ClassRunner.class)
public class SolrControllerTests extends AbstractContext {

    private SolrController solrController;
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Before
    public void setUp() {
        solrController = new SolrController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(solrController).build();
    }

    @Test
    public void getProducts() throws Exception {

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("contacts"))
//                .andExpect(model().attribute("contacts",
//                        hasItems(allContacts.toArray())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

    }

}