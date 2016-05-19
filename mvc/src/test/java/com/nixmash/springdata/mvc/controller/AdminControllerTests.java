package com.nixmash.springdata.mvc.controller;

import com.github.dandelion.core.web.DandelionFilter;
import com.nixmash.springdata.jpa.common.ISiteOption;
import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.dto.SiteOptionMapDTO;
import com.nixmash.springdata.jpa.service.SiteService;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.security.WithAdminUser;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;

import static com.nixmash.springdata.jpa.model.SiteOptionTests.*;
import static com.nixmash.springdata.mvc.controller.AdminController.ADMIN_HOME_VIEW;
import static com.nixmash.springdata.mvc.controller.AdminController.ADMIN_USERS_VIEW;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
public class AdminControllerTests extends AbstractContext {

    private AdminController adminController;
    private UserService mockUserService;
    private DandelionFilter dandelionFilter;

    private static final String NEW_SITE_NAME = "New Site Name";
    private static final Integer NEW_INTEGER_PROPERTY = 8;


    @Autowired
    private WebUI webUI;

    @Autowired
    private UserService userService;

    @Autowired
    private SiteOptions siteOptions;

    @Autowired
    private SiteService siteService;

    private MockMvc mvc;
    private SiteOptionMapDTO siteOptionMapDTO;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() throws ServletException {

        this.dandelionFilter = new DandelionFilter();
        this.dandelionFilter.init(new MockFilterConfig());

        mvc = webAppContextSetup(wac)
                .apply(springSecurity())
                .addFilters(dandelionFilter)
                .build();

        siteOptionMapDTO = SiteOptionMapDTO.withGeneralSettings(
                siteOptions.getSiteName(),
                siteOptions.getSiteDescription(),
                siteOptions.getAddGoogleAnalytics(),
                siteOptions.getGoogleAnalyticsTrackingId())
                .build();

        mockUserService = mock(UserService.class);
        adminController = new AdminController(userService, webUI, siteOptions, siteService);
    }

    @After
    public void tearDown() {
        siteOptions.setSiteName(DEFAULT_SITE_NAME);
        siteOptions.setSiteDescription(DEFAULT_SITE_DESCRIPTION);
        siteOptions.setAddGoogleAnalytics(false);
        siteOptions.setGoogleAnalyticsTrackingId(DEFAULT_TRACKING_ID);
    }


    @Test
    @WithAdminUser
    public void adminUserCanAccessAdminDashboard() throws Exception {
        RequestBuilder request = get("/admin").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_HOME_VIEW));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "currentUserDetailsService")
    public void nonAdminCannotAccessAdminDashboard() throws Exception {
        RequestBuilder request = get("/admin").with(csrf());
        mvc.perform(request)
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/403"));
    }

    @Test
    @WithAdminUser
    public void adminUserCanAccessAdminUsersList() throws Exception {
        RequestBuilder request = get("/admin/users").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_USERS_VIEW));
    }

    @Test
    @WithAdminUser
    public void retrieveSiteOptionsForSiteGeneralSettingsPage() throws Exception {
        RequestBuilder request = get("/admin/site/settings").with(csrf());
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("siteOptionMapDTO"))
                .andExpect(view().name(AdminController.ADMIN_SITESETTINGS_VIEW));
    }

    @Test
    @WithAdminUser
    public void siteSettingsWithEmptySiteName_ErrorResult() throws Exception {

        RequestBuilder request = post("/admin/site/settings")
                .param(ISiteOption.SITE_NAME, StringUtils.EMPTY)
                .param(ISiteOption.SITE_DESCRIPTION, siteOptionMapDTO.getSiteDescription())
                .param(ISiteOption.ADD_GOOGLE_ANALYTICS, String.valueOf(siteOptionMapDTO.getAddGoogleAnalytics()))
                .param(ISiteOption.GOOGLE_ANALYTICS_TRACKING_ID, siteOptionMapDTO.getGoogleAnalyticsTrackingId()).with(csrf());

        mvc.perform(request)
                .andExpect(model().attributeHasFieldErrors("siteOptionMapDTO", "siteName"))
                .andExpect(view().name(AdminController.ADMIN_SITESETTINGS_VIEW));
    }

    @Test
    public void updateGeneralSiteSettingsMethodTest() throws Exception {
        siteOptionMapDTO.setSiteName(NEW_SITE_NAME);
        siteOptionMapDTO.setIntegerProperty(NEW_INTEGER_PROPERTY);

        adminController.updateGeneralSiteSettings(siteOptionMapDTO);

        assertEquals(siteOptions.getSiteName(), NEW_SITE_NAME);

        // integerProperty is not updated as a General Site Setting
        assertEquals(siteOptions.getIntegerProperty(), DEFAULT_INTEGER_PROPERTY);
    }

    @Test
    @WithAdminUser
    public void siteSettingsUpdated_UpdatesSiteOptions() throws Exception {

        RequestBuilder request = post("/admin/site/settings")
                .param(ISiteOption.SITE_NAME, siteOptionMapDTO.getSiteName())
                .param(ISiteOption.SITE_DESCRIPTION, siteOptionMapDTO.getSiteDescription())
                .param(ISiteOption.ADD_GOOGLE_ANALYTICS, String.valueOf(siteOptionMapDTO.getAddGoogleAnalytics()))
                .param(ISiteOption.GOOGLE_ANALYTICS_TRACKING_ID, siteOptionMapDTO.getGoogleAnalyticsTrackingId()).with(csrf());

        mvc.perform(request)
                .andExpect(model().attributeHasNoErrors())
                .andExpect(MockMvcResultMatchers.flash().attributeExists("feedbackMessage"))
                .andExpect(redirectedUrl("/admin/site/settings"));
    }


}
