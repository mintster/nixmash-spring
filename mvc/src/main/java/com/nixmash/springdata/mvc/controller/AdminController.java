package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private static final String ADMIN_MOCKUP_VIEW = "admin/mockup";
    private static final String ADMIN_HOME_VIEW = "admin/dashboard";
    private static final String ADMIN_USERS_VIEW = "admin/security/users";
    private static final String ADMIN_ROLES_VIEW = "admin/security/roles";

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    // region Main Pages

    @RequestMapping(value = "", method = GET)
    public String home(Model model) {
        return ADMIN_HOME_VIEW;
    }

    @RequestMapping(value = "/mockup", method = GET)
    public String mockup(Model model) {
        return ADMIN_MOCKUP_VIEW;
    }

    // endregion

    // region Security

    @RequestMapping(value = "/security/users", method = GET)
    public String userlist(Model model) {
        return ADMIN_USERS_VIEW;
    }

    @RequestMapping(value = "/security/roles", method = GET)
    public String roleslist(Model model) {
        return ADMIN_ROLES_VIEW;
    }

    // endregion

}
