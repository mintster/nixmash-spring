/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.ForgotEmailDTO;
import com.nixmash.springdata.jpa.dto.UserPasswordDTO;
import com.nixmash.springdata.jpa.enums.ResetPasswordResult;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.UserToken;
import com.nixmash.springdata.jpa.model.validators.UserPasswordValidator;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.jpa.utils.SharedUtils;
import com.nixmash.springdata.mail.service.FmMailService;
import com.nixmash.springdata.mvc.components.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

import static com.nixmash.springdata.mvc.components.WebUI.FLASH_MESSAGE_KEY_FEEDBACK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserPasswordController {

    // region Constants

    public static final String USER_CHANGEPASSWORD_VIEW = "users/password";
    private static final String FEEDBACK_PASSWORD_RESET = "feedback.user.password.reset";
    private static final String FEEDBACK_MESSAGE_PASSWORD_ERROR = "feedback.user.password.error";
    private static final String FEEDBACK_PASSWORD_LOGIN = "feedback.user.password.login";
    public static final String USER_FORGOTPASSWORD_VIEW = "users/forgot";
    private static final String FEEDBACK_PASSWORD_EMAIL_SENT = "feedback.user.password.email.sent";

    // endregion

    // region Private Classes

    private final UserService userService;
    private final UserPasswordValidator userPasswordValidator;
    private WebUI webUI;
    private final FmMailService fmMailService;

    // endregion

    private static final Logger logger = LoggerFactory.getLogger(UserPasswordController.class);

    @Autowired
    public UserPasswordController(UserService userService,
                                  UserPasswordValidator userPasswordValidator,
                                  WebUI webUI,
                                  FmMailService fmMailService) {
        this.userService = userService;
        this.userPasswordValidator = userPasswordValidator;
        this.webUI = webUI;
        this.fmMailService = fmMailService;
    }

    @InitBinder("userPasswordDTO")
    public void initUserPasswordBinder(WebDataBinder binder) {
        binder.addValidators(userPasswordValidator);
    }

    @RequestMapping(value = "/users/forgotpassword", method = GET)
    public String sendForgotEmail(Model model) {
        model.addAttribute("forgotEmailDTO", new ForgotEmailDTO());
        return USER_FORGOTPASSWORD_VIEW;
    }

    @RequestMapping(value = "/users/forgotpassword", method = POST)
    public String sendForgotEmail(@Valid ForgotEmailDTO forgotEmailDTO,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return USER_FORGOTPASSWORD_VIEW;
        } else {
            Optional<User> user = userService.getByEmail(forgotEmailDTO.getEmail());
            if (!user.isPresent()) {
                result.reject("global.error.email.does.not.exist");
            } else {

                model.addAttribute(FLASH_MESSAGE_KEY_FEEDBACK,
                        webUI.getMessage(FEEDBACK_PASSWORD_EMAIL_SENT));
                model.addAttribute("forgotEmailDTO", new ForgotEmailDTO());

                UserToken userToken = userService.createUserToken(user.get());
                fmMailService.sendResetPasswordMail(user.get(), userToken.getToken());
            }
        }
        return USER_FORGOTPASSWORD_VIEW;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/users/resetpassword", method = GET)
    public String changePassword(CurrentUser currentUser, Model model) {

        UserPasswordDTO userPasswordDTO = new UserPasswordDTO(currentUser.getId(), UUID.randomUUID().toString());
        model.addAttribute("userPasswordDTO", userPasswordDTO);
        return USER_CHANGEPASSWORD_VIEW;
    }

    @RequestMapping(value = "/users/resetpassword/{token}", method = GET)
    public String changePasswordFromEmail(@PathVariable String token, Model model) {
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO(SharedUtils.randomNegativeId(), token);
        model.addAttribute("userPasswordDTO", userPasswordDTO);
        return USER_CHANGEPASSWORD_VIEW;
    }


    @RequestMapping(value = "/users/resetpassword", method = POST)
    public ModelAndView changePassword(@Valid @ModelAttribute("userPasswordDTO")
                                               UserPasswordDTO userPasswordDTO, BindingResult result) {
        ModelAndView mav = new ModelAndView();
        if (!result.hasErrors()) {

            ResetPasswordResult resetPasswordResult =
                    userService.updatePassword(userPasswordDTO);

            switch (resetPasswordResult) {
                case ERROR:
                    result.reject("global.error.password.reset");
                    break;
                case FORGOT_SUCCESSFUL:
                    mav.addObject(FLASH_MESSAGE_KEY_FEEDBACK,
                            webUI.getMessage(FEEDBACK_PASSWORD_LOGIN));
                    break;
                case LOGGEDIN_SUCCESSFUL:
                    mav.addObject(FLASH_MESSAGE_KEY_FEEDBACK,
                            webUI.getMessage(FEEDBACK_PASSWORD_RESET));
                    break;
            }
        }

        mav.addObject("userPasswordDTO", userPasswordDTO);
        mav.setViewName(USER_CHANGEPASSWORD_VIEW);
        return mav;

    }

}
