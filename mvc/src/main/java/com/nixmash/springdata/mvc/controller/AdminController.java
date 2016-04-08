package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private static final String ADMIN_MOCKUP_VIEW = "admin/mockup";
    private static final String ADMIN_HOME_VIEW = "admin/dashboard";
    private static final String ADMIN_USERS_VIEW = "admin/security/users";
    private static final String ADMIN_ROLES_VIEW = "admin/security/roles";
    private static final String ADMIN_USERFORM_VIEW = "admin/security/userform";

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

    @RequestMapping(value = "/users", method = GET)
    public ModelAndView userlist(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userService.getAllUsers());
        mav.setViewName(ADMIN_USERS_VIEW);
        return mav;
    }

    @RequestMapping(value = "/users/update/{userId}", method = GET)
    public ModelAndView userlist(@PathVariable("userId") Long id, Model model) {

        Optional<User> user = userService.getUserById(id);
        logger.info("Editing User with id and username: {} {}", id, user.get().getUsername());

        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user.get());
        mav.setViewName(ADMIN_USERFORM_VIEW);
        return mav;
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.GET)
    public String initAddContactForm(Model model) {
        User  user = new User();
        model.addAttribute(user);
        return ADMIN_USERFORM_VIEW;
    }

    @RequestMapping(value = "/roles", method = GET)
    public ModelAndView roleList(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("roles", userService.getRoles());
        mav.setViewName(ADMIN_ROLES_VIEW);
        return mav;
    }

    // endregion

}
