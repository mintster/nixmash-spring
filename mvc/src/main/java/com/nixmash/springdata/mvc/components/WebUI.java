package com.nixmash.springdata.mvc.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.dto.GitHubDTO;
import com.nixmash.springdata.jpa.dto.ProfileImageDTO;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

@Component
public class WebUI {

    private static final Logger logger = LoggerFactory.getLogger(WebUI.class);

    public static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    @Resource
    private MessageSource messageSource;

    @Autowired
    Environment environment;

    private final ApplicationSettings applicationSettings;

    @Autowired
    public WebUI(ApplicationSettings applicationSettings) {
        this.applicationSettings = applicationSettings;
    }

    // region Message Functions

    public String getMessage(String code, Object... params) {
        Locale current = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, params, current);
    }

    public void addFeedbackMessage(RedirectAttributes model, String code, Object... params) {
        logger.info("Adding feedback message with code: {} and params: {}", code, params);
        String localizedFeedbackMessage = getMessage(code, params);
        logger.info("Localized message is: {}", localizedFeedbackMessage);
        model.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
    }

    public String parameterizedMessage(String code, Object... params) {
        logger.info("Adding paramertized message with code: {} and params: {}", code, params);
        String localizedMessage = getMessage(code, params);
        logger.info("Localized message is: {}", localizedMessage);
        return localizedMessage;
    }

    // endregion

    // region MultiFile Upload and Image Functions

    public void processProfileImage(ProfileImageDTO profileImageDTO, String userKey)
            throws IOException {

        // Reduce original image size. Thumbnailator will not modify
        // image if less than 600x600

        BufferedImage bufferedProfileImage =
                Thumbnails.of(profileImageDTO.getFile().getInputStream())
                        .forceSize(600, 600)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();

        saveProfileImage(bufferedProfileImage, userKey, false);

        // Create profile image icon. Saved to separate directory

        BufferedImage bufferedIconImage =
                Thumbnails.of(profileImageDTO.getFile().getInputStream())
                        .forceSize(32, 32)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();

        saveProfileImage(bufferedIconImage, userKey, true);
    }


    public void processProfileImage(String providerImageUrl, String userKey)
            throws IOException {

        // Reduce original image size. Thumbnailator will not modify
        // image if less than 600x600

        BufferedImage bufferedProfileImage =
                Thumbnails.of(new URL(providerImageUrl))
                        .forceSize(600, 600)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();

        saveProfileImage(bufferedProfileImage, userKey, false);

        // Create profile image icon. Saved to separate directory

        BufferedImage bufferedIconImage =
                Thumbnails.of(new URL(providerImageUrl))
                        .forceSize(32, 32)
                        .allowOverwrite(true)
                        .outputFormat("png")
                        .asBufferedImage();

        saveProfileImage(bufferedIconImage, userKey, true);
    }

    private void saveProfileImage(BufferedImage bufferedImage, String userKey, boolean isIcon) throws IOException {

        String destination = isIcon ? applicationSettings.getProfileIconPath() : applicationSettings.getProfileImagePath();
        File imageDestination = new File(destination + userKey);
        ImageIO.write(bufferedImage, "png", imageDestination);

    }

    // endregion

    // region GitHub Statistics

    public GitHubDTO getGitHubStats() {

        String gitHubRepoUrl = environment.getProperty("github.repo.url");
        String gitHubUserUrl = environment.getProperty("github.user.url");

        // Load Repository JSON elements into GitHubDTO Object

        RestTemplate restTemplate = new RestTemplate();
        GitHubDTO gitHubDTO = restTemplate.getForObject(gitHubRepoUrl, GitHubDTO.class);

        // Load User Followers count from GitHub User JSON Endpoint and add to GitHubDTO

        HttpEntity<String> stringUserEntity = restTemplate.getForEntity(gitHubUserUrl, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode node = mapper.readValue(stringUserEntity.getBody(), ObjectNode.class);
            gitHubDTO.setFollowers(node.get("followers").intValue());
        } catch (IOException e) {
           logger.error("Error adding follower count from GitHub API to GitHubDTO object");
        }
        return gitHubDTO;
    }

    // endregion

}

