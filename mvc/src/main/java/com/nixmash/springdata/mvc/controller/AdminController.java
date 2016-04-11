package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.UserUtils;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.enums.SignInProvider;
import com.nixmash.springdata.jpa.model.Authority;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.common.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private static final String ADMIN_MOCKUP_VIEW = "admin/mockup";
    private static final String ADMIN_HOME_VIEW = "admin/dashboard";
    private static final String ADMIN_USERS_VIEW = "admin/security/users";
    private static final String ADMIN_ROLES_VIEW = "admin/security/roles";
    private static final String ADMIN_USERFORM_VIEW = "admin/security/userform";
    private static final String PARAMETER_USER_ID = "id";

    protected static final String FEEDBACK_MESSAGE_KEY_USER_UPDATED = "feedback.message.user.updated";
    private static final String FEEDBACK_MESSAGE_KEY_USER_ADDED = "feedback.message.user.added";

    private final UserService userService;
    private final WebUI webUI;

    @Autowired
    public AdminController(UserService userService, WebUI webUI) {
        this.userService = userService;
        this.webUI = webUI;
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
    public ModelAndView updateUser(@PathVariable("userId") Long id) {
        return populateUserForm(id);
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.GET)
    public ModelAndView initAddUserForm() {
        return populateUserForm((long)-1);
    }

    private ModelAndView populateUserForm(Long id) {

        ModelAndView mav = new ModelAndView();
        Optional<User> found = userService.getUserById(id);
        User user = new User();
        if (found.isPresent()) {
            user = found.get();
            logger.info("Editing User with id and username: {} {}", id, user.getUsername());
            mav.addObject("user", UserUtils.userToUserDTO(user));
        }
        else
        {
            mav.addObject("user", new UserDTO());
        }
        mav.addObject("authorities", userService.getRoles());
        mav.setViewName(ADMIN_USERFORM_VIEW);
        return mav;
    }

    @RequestMapping(value = "/users/update/{userId}", method = RequestMethod.POST)
    public String updateUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result,
                                RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            return ADMIN_USERFORM_VIEW;
        } else {

            userDTO.setUpdateChildren(true);
            userService.update(userDTO);

            attributes.addAttribute(PARAMETER_USER_ID, userDTO.getUserId());
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_USER_UPDATED, userDTO.getFirstName(),
                    userDTO.getLastName());

            return "redirect:/admin/users";
        }
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.POST)
    public String addUser(@Valid UserDTO userDTO, BindingResult result, SessionStatus status,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return ADMIN_USERFORM_VIEW;
        } else {
            userDTO.setPassword(UUID.randomUUID().toString());
            userDTO.setSignInProvider(SignInProvider.SITE);
            User added = userService.create(userDTO);
            logger.info("Added user with information: {}", added);
            status.setComplete();

            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_USER_ADDED, added.getFirstName(),
                    added.getLastName());

            return "redirect:/admin/users";
        }
    }

    @RequestMapping(value = "/roles", method = GET)
    public ModelAndView roleList(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("roles", userService.getRoles());
        mav.addObject("newRole", new Authority());
        mav.setViewName(ADMIN_ROLES_VIEW);
        return mav;
    }

    // endregion

}
