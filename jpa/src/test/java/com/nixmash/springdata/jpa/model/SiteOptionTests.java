package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.dto.SiteOptionDTO;
import com.nixmash.springdata.jpa.dto.SiteOptionMapDTO;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.springdata.jpa.repository.SiteOptionRepository;
import com.nixmash.springdata.jpa.service.SiteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class SiteOptionTests {

    // region Constants

    private static final String MY_SITE_NAME = "My Site";
    private static final String MY_UPDATED_SITE_NAME = "My Updated Site Name";
    private static final Integer INTEGER_PROPERTY = 1;
    private static final Integer UPDATED_INTEGER_PROPERTY = 8;

    // endregion

    // region Beans

    @Autowired
    protected ApplicationContext context;

    @Autowired
    SiteOptionRepository siteOptionRepository;

    @Autowired
    SiteOptions siteOptions;

    @Autowired
    SiteService siteService;

    // endregion

    // region Private Properties

    private SiteOptionMapDTO siteOptionMapDTO;
    // endregion

    @Test
    public void siteOptionsIsPopulatedFromContext() throws Exception {
        assertEquals(siteOptions.getAddGoogleAnalytics(), false);
        assertEquals(siteOptions.getIntegerProperty(), (Integer) 1);
        assertEquals(siteOptions.getGoogleAnalyticsTrackingId(), "UA-XXXXXX-7");
        assertEquals(siteOptions.getSiteName(), "My Site");
        assertEquals(siteOptions.getSiteDescription(), "My Site Description");
    }

    @Test
    public void siteOptionsPropertyIsUpdatedAtRuntime() {

        assertEquals(siteOptions.getSiteName(), MY_SITE_NAME);
        assertEquals(siteOptions.getIntegerProperty(), INTEGER_PROPERTY);

        try {

            siteService.update(new SiteOptionDTO("siteName", MY_UPDATED_SITE_NAME));
            siteService.update(new SiteOptionDTO("integerProperty",
                    UPDATED_INTEGER_PROPERTY.toString()));

        } catch (SiteOptionNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(siteOptions.getSiteName(), MY_UPDATED_SITE_NAME);
        assertEquals(siteOptions.getIntegerProperty(), UPDATED_INTEGER_PROPERTY);
    }

    @Test
    public void siteOptionDtoCreatedFromSiteOptionMapDTO() {
        SiteOptionDTO siteOptionDTO = SiteOptionDTO.with("siteName", "My Fabulous Site").build();
        assertEquals(siteOptionDTO.getName(), "siteName");
        assertEquals(siteOptionDTO.getValue(), "My Fabulous Site");
    }

}
