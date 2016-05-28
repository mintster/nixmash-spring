package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.exceptions.ContactNotFoundException;
import com.nixmash.springdata.jpa.exceptions.ResourceNotFoundException;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.mvc.components.WebUI;
import com.nixmash.springdata.solr.exceptions.GeoLocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.ConnectionData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class  GlobalController {

	private static final Logger logger = LoggerFactory.getLogger(GlobalController.class);

 	protected static final String ERROR_CUSTOM_VIEW = "errors/custom";

	private static final String PRODUCT_MAP_VIEW = "products/map";
	private static final String LOCATION_ERROR_MESSAGE_KEY = "product.map.page.feedback.error";
	public static final String LOCATION_ERROR_ATTRIBUTE = "mappingError";
	public static final String SESSION_USER_CONNECTION = "MY_USER_CONNECTION";

	@Autowired
	WebUI webUI;

	@Autowired
	private ApplicationSettings applicationSettings;

	@Autowired
	private SiteOptions siteOptions;

	@ModelAttribute("currentUser")
	public CurrentUser getCurrentUser(Authentication authentication) {
		CurrentUser currentUser = null;
		if (authentication == null)
			return null;
		else {
			currentUser = (CurrentUser) authentication.getPrincipal();
		}
		return currentUser;
	}

	/**
	 * Determines if the CurrentUser is "user".
	 * For enabling or disabling certain user account functions
	 *
	 * @param authentication
	 * @return TRUE is username=="user", otherwise FALSE
     */
	@ModelAttribute("isDemoUser")
	public Boolean isDemoUser(Authentication authentication) {
		Boolean isDemoUser = false;
		if (authentication != null) {
			if (authentication.getName().equals("user")) {
				isDemoUser = true;
			}
		}
		return isDemoUser;
	}

	@ModelAttribute("currentUserConnection")
	public ConnectionData getUserConnection(WebRequest request) {
		return (ConnectionData) request.getAttribute(SESSION_USER_CONNECTION,
				RequestAttributes.SCOPE_SESSION);
	}

	@ModelAttribute("appSettings")
	public ApplicationSettings getApplicationSettings() {
		return applicationSettings;
	}

	@ModelAttribute("siteOptions")
	public SiteOptions getSiteOptions() {
		return siteOptions;
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleResourceNotFoundException() {
		return "errors/404";
	}

	@ExceptionHandler(ContactNotFoundException.class)
	public ModelAndView handleContactNotFoundException() {
		logger.info("In ContactNotFound Exception Handler");

		ModelAndView mav = new ModelAndView();
		mav.addObject("errortitle", "Contact Missing in Action!");
		mav.addObject("errorbody", "We'll find the rascal, don't you worry");
		mav.setViewName(ERROR_CUSTOM_VIEW);
		return mav;
	}

	@ExceptionHandler(GeoLocationException.class)
	public ModelAndView handleGeoLocationException(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String location = (String) request.getAttribute("location");
		String msg = webUI.getMessage(LOCATION_ERROR_MESSAGE_KEY, location);
		mav.addObject(LOCATION_ERROR_ATTRIBUTE, msg);
		mav.setViewName(PRODUCT_MAP_VIEW);
		return mav;
	}

}
