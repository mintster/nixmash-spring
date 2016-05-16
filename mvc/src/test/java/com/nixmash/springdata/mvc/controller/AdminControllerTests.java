package com.nixmash.springdata.mvc.controller;

import com.github.dandelion.core.web.DandelionFilter;
import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.dto.SiteOptionMapDTO;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.AbstractContext;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.mvc.security.WithAdminUser;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;

import static com.nixmash.springdata.mvc.controller.AdminController.ADMIN_HOME_VIEW;
import static com.nixmash.springdata.mvc.controller.AdminController.ADMIN_USERS_VIEW;
import static com.nixmash.springdata.mvc.security.SecurityRequestPostProcessors.csrf;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
public class AdminControllerTests extends AbstractContext {

    private AdminController mockAdminController;
    private UserService mockUserService;
    private DandelionFilter dandelionFilter;

    @Autowired
    private WebUI webUI;

    @Autowired
    private UserService userService;

    @Autowired
    private SiteOptions siteOptions;

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

        siteOptionMapDTO = SiteOptionMapDTO.with(
                siteOptions.getSiteName(),
                siteOptions.getSiteDescription(),
                siteOptions.getAddGoogleAnalytics(),
                siteOptions.getGoogleAnalyticsTrackingId())
                .build();

        mockUserService = mock(UserService.class);
        mockAdminController = new AdminController(userService, webUI, siteOptions);
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
    public void retrieveSiteOptionsForSiteSettingsPage() throws Exception {
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
                .param("siteName", StringUtils.EMPTY)
                .param("siteDescription", siteOptionMapDTO.getSiteDescription())
                .param("addGoogleAnalytics", String.valueOf(siteOptionMapDTO.getAddGoogleAnalytics()))
                .param("googleAnalyticsTrackingId", siteOptionMapDTO.getGoogleAnalyticsTrackingId()).with(csrf());

        mvc.perform(request)
                .andExpect(model().attributeHasFieldErrors("siteOptionMapDTO", "siteName"))
                .andExpect(view().name(AdminController.ADMIN_SITESETTINGS_VIEW));
    }

    @Test
    @WithAdminUser
    public void siteSettingsUpdated_UpdatesSiteOptions() throws Exception {

        RequestBuilder request = post("/admin/site/settings")
                .param("siteName", siteOptionMapDTO.getSiteName())
                .param("siteDescription", siteOptionMapDTO.getSiteDescription())
                .param("addGoogleAnalytics", String.valueOf(siteOptionMapDTO.getAddGoogleAnalytics()))
                .param("googleAnalyticsTrackingId", siteOptionMapDTO.getGoogleAnalyticsTrackingId()).with(csrf());

        mvc.perform(request)
                .andExpect(model().attributeHasNoErrors())
                .andExpect(view().name("redirect:/admin/site/settings"));
    }


}
