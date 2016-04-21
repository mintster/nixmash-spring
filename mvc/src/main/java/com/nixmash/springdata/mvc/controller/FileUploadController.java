package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.ProfileImageDTO;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.validators.ProfileImageValidator;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.components.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

import static com.nixmash.springdata.mvc.controller.UserController.USER_PROFILE_VIEW;

@Controller
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private static final String FEEDBACK_MESSAGE_KEY_PROFILE_IMAGE_UPDATED = "feedback.message.profile.image.updated";
    private static final String FEEDBACK_MESSAGE_KEY_PROFILE_IMAGE_REMOVED = "feedback.message.profile.image.removed";

    private WebUI webUI;
    private ProfileImageValidator profileImageValidator;
    private UserService userService;

    @Autowired
    public FileUploadController(WebUI webUI, ProfileImageValidator profileImageValidator,
                                UserService userService) {
        this.webUI = webUI;
        this.profileImageValidator = profileImageValidator;
        this.userService = userService;
    }

    @InitBinder("profileImageDTO")
    protected void initBinderProfileImage(WebDataBinder binder) {
        binder.setValidator(profileImageValidator);
    }

    @RequestMapping(value = "/users/upload", params = {"deleteImage"}, method = RequestMethod.POST)
    public String deleteProfileImage(CurrentUser currentUser, RedirectAttributes attributes,
                                     Model model, SessionStatus status) {

        logger.info("Removing Profile Image for user: {}", currentUser.getUser().getUsername());
        String profileRedirectUrl = String.format("redirect:/%s", currentUser.getUsername());

        ProfileImageDTO profileImageDTO = new ProfileImageDTO();
        model.addAttribute("profileImageDTO", profileImageDTO);

        userService.updateHasAvatar(currentUser.getId(), false);
        status.setComplete();

        webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_PROFILE_IMAGE_REMOVED);
        return profileRedirectUrl;
    }

    @RequestMapping(value = "/users/upload", method = RequestMethod.POST)
    public String handleFileUpload(@Valid ProfileImageDTO profileImageDTO,
                                   BindingResult result, ModelMap model, CurrentUser currentUser,
                                   RedirectAttributes attributes, SessionStatus status) throws IOException {

        String profileRedirectUrl = String.format("redirect:/%s?ico", currentUser.getUsername());

        if (result.hasErrors()) {
            logger.info("Profile Image Errors for: {}", profileImageDTO.getFile().getOriginalFilename());
            return USER_PROFILE_VIEW;

        } else {
            logger.info("Fetching file");
            MultipartFile multipartFile = profileImageDTO.getFile();
            String userKey = currentUser.getUser().getUserKey();
            webUI.processProfileImage(profileImageDTO, userKey);
            userService.updateHasAvatar(currentUser.getId(), true);
            status.setComplete();

            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_PROFILE_IMAGE_UPDATED);
            return profileRedirectUrl;
        }
    }

}
