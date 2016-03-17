package com.nixmash.springdata.mvc.common;

import java.util.Locale;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Component
public class WebUI {

	private static final Logger logger = LoggerFactory.getLogger(WebUI.class);

	public static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

	@Resource
	private MessageSource messageSource;

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

}
