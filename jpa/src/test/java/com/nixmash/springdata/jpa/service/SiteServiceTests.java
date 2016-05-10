package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.SiteOptionDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.springdata.jpa.model.SiteOption;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class SiteServiceTests {

    private static final String VALID_PROPERTY_NAME = "SiteName";
    private static final String INVALID_PROPERTY_NAME = "SchmiteName";

    @Autowired
    SiteService siteService;

    @Autowired
    SiteOptions siteOptions;

    @Test
    public void findSiteOptionByCaseInsensitivePropertyName() throws NotFoundException, SiteOptionNotFoundException {
        SiteOption siteOption;

        siteOption = siteService.findOptionByName(VALID_PROPERTY_NAME);
        assertEquals(siteOption.getValue(), "My Site");

        siteOption = siteService.findOptionByName(VALID_PROPERTY_NAME.toLowerCase());
        assertEquals(siteOption.getValue(), "My Site");
    }

    @Test(expected = SiteOptionNotFoundException.class)
    public void invalidPropertyNameThrowsSiteOptionNotFoundException() throws Exception {
        siteService.findOptionByName(INVALID_PROPERTY_NAME);
    }

    @Test
    public void siteOptionUpdateNotReflectedInSiteOptionsComponent() throws SiteOptionNotFoundException {
        SiteOptionDTO siteOptionDTO = new SiteOptionDTO(VALID_PROPERTY_NAME, "Updated Site Name");
        siteService.update(siteOptionDTO);
        SiteOption found = siteService.findOptionByName(VALID_PROPERTY_NAME);
        assertEquals(found.getValue(), "Updated Site Name");

        System.out.println(siteOptions.getSiteName());
    }

    @Test
    public void updateSiteOption() throws SiteOptionNotFoundException {

    }

}