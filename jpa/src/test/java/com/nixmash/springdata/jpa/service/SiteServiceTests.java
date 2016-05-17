package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.common.ISiteOption;
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

    private static final String SITE_PROPERTY_NAME = "siteName";
    private static final String INTEGER_PROPERTY_NAME = "integerProperty";
    private static final String INVALID_PROPERTY_NAME = "SchmiteName";

    @Autowired
    SiteService siteService;

    @Autowired
    SiteOptions siteOptions;

    @Test
    public void findSiteOptionByCaseInsensitivePropertyName() throws NotFoundException, SiteOptionNotFoundException {
        SiteOption siteOption;

        siteOption = siteService.findOptionByName(SITE_PROPERTY_NAME);
        assertEquals(siteOption.getValue(), "My Site");

        siteOption = siteService.findOptionByName(SITE_PROPERTY_NAME.toLowerCase());
        assertEquals(siteOption.getValue(), "My Site");
    }

    @Test(expected = SiteOptionNotFoundException.class)
    public void invalidPropertyNameThrowsSiteOptionNotFoundException() throws Exception {
        siteService.findOptionByName(INVALID_PROPERTY_NAME);
    }

    @Test
    public void siteOptionUpdated_UpdatesSiteOptionsBean() throws SiteOptionNotFoundException {
        siteService.update(new SiteOptionDTO(SITE_PROPERTY_NAME, "Updated Site Name"));
        siteService.update(new SiteOptionDTO(INTEGER_PROPERTY_NAME, "8"));

        assert(siteOptions.getSiteName().equals("Updated Site Name"));
        assert(siteOptions.getIntegerProperty().equals(8));

    }

    @Test
    public void updateSiteOptionWithSiteOptionDTO() throws SiteOptionNotFoundException {

        SiteOptionDTO siteOptionDTO;
        siteOptionDTO = SiteOptionDTO.with(ISiteOption.SITE_NAME, "My Fabulous Site").build();
        siteService.update(siteOptionDTO);
        assert(siteOptions.getSiteName().equals("My Fabulous Site"));

        siteOptionDTO = SiteOptionDTO.with(ISiteOption.SITE_NAME, null).build();
        siteService.update(siteOptionDTO);
    }

}