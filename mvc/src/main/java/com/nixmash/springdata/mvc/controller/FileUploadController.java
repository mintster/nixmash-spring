package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.ProfileImageDTO;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.validators.ProfileImageValidator;
import com.nixmash.springdata.jpa.service.UserService;
import com.nixmash.springdata.mvc.common.WebUI;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.nixmash.springdata.mvc.controller.UserController.USER_PROFILE_VIEW;

@Controller
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private static final String FEEDBACK_MESSAGE_KEY_PROFILE_IMAGE_UPDATED = "feedback.message.profile.image.updated";

    private WebUI webUI;
    private ProfileImageValidator profileImageValidator;
    private UserService userService;

    @Value("#{applicationSettings.profileImagePath}")
    private String PROFILE_IMAGE_LOCATION;

    @Value("#{applicationSettings.profileIconPath}")
    private String PROFILE_ICON_LOCATION;

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

    @RequestMapping(value = "/users/upload", method = RequestMethod.POST)
    public String handleFileUpload(@Valid ProfileImageDTO profileImageDTO,
                                   BindingResult result, ModelMap model, CurrentUser currentUser,
                                   RedirectAttributes attributes, SessionStatus status) throws IOException {

        String profileRedirectUrl = String.format("redirect:/%s", currentUser.getUsername());

        if (result.hasErrors()) {
            logger.info("Profile Image Errors for: {}", profileImageDTO.getFile().getOriginalFilename());
            return USER_PROFILE_VIEW;

        } else {
            logger.info("Fetching file");
            MultipartFile multipartFile = profileImageDTO.getFile();
            String filename = currentUser.getUser().getUserKey();

//            FileCopyUtils.copy(profileImageDTO.getFile().getBytes(), new File(PROFILE_IMAGE_LOCATION + filename));

            File profileImageDestination = new File(PROFILE_IMAGE_LOCATION + filename);
            File iconDestination = new File(PROFILE_ICON_LOCATION + filename);

            InputStream imageStream = profileImageDTO.getFile().getInputStream();

            BufferedImage bufferedProfileImage=
                    Thumbnails.of(profileImageDTO.getFile().getInputStream())
                    .forceSize(600, 600)
                    .allowOverwrite(true)
                    .outputFormat("png")
                    .asBufferedImage();
            ImageIO.write(bufferedProfileImage, "png", profileImageDestination);

            BufferedImage bufferedIconImage =
                    Thumbnails.of(profileImageDTO.getFile().getInputStream())
                    .forceSize(32, 32)
                    .allowOverwrite(true)
                    .outputFormat("png")
                    .asBufferedImage();
            ImageIO.write(bufferedIconImage, "png", iconDestination);

            userService.updateHasAvatar(currentUser.getId(), true);
            status.setComplete();

            webUI.addFeedbackMessage(attributes,FEEDBACK_MESSAGE_KEY_PROFILE_IMAGE_UPDATED);
            return profileRedirectUrl;
        }
    }
}
