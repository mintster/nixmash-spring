package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.SelectOptionDTO;
import com.nixmash.springdata.jpa.exceptions.UnknownResourceException;
import com.nixmash.springdata.mvc.common.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class GeneralController {

    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);

    public static final String HOME_VIEW = "home";
    public static final String ERROR_403_VIEW = "errors/403";

    @Autowired
    WebUI webUI;

    @RequestMapping(value = "/", method = GET)
    public String home(Model model) {
        return HOME_VIEW;
    }

    @RequestMapping(value = {"{path:(?!webjars|static|console).*$}",
            "{path:(?!webjars|static|console).*$}/**"}, headers = "Accept=text/html")
    public void unknown() {
        throw new UnknownResourceException();
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ModelAndView accesssDenied(Principal user) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("errortitle", "Not Authorized");
        mav.addObject("errorbody", "You are not authorized to view this page.");
        mav.setViewName(ERROR_403_VIEW);
        return mav;

    }

    @RequestMapping(value = "/json/badges/update",
            method = RequestMethod.POST)
    public
    @ResponseBody
    String updateBadges(@RequestBody List<String> badgeboys) {
        if (badgeboys != null) {
            String badges = badgeboys.stream().collect(joining(", "));
            logger.info("Badge Boy Items: " + badges);
            return webUI.getMessage("js.badgeboy.result", badges);
        } else
            return "No badges selected...";
    }

    @RequestMapping(value = "/json/badges", method = RequestMethod.GET)
    public
    @ResponseBody
    List<SelectOptionDTO> getBadges() {
        return badgeSelectOptions();
    }

    private List<SelectOptionDTO> badgeSelectOptions() {
        List<SelectOptionDTO> selectOptionDTOs = new ArrayList<>();
        selectOptionDTOs.add(new SelectOptionDTO("Innovator", "Innovator", false));
        selectOptionDTOs.add(new SelectOptionDTO("Marathoner", "Marathoner", true));
        selectOptionDTOs.add(new SelectOptionDTO("Dude of Action", "Dude of Action", false));
        selectOptionDTOs.add(new SelectOptionDTO("Worldly", "Worldly", false));
        selectOptionDTOs.add(new SelectOptionDTO("Swell Guy", "Swell Guy", false));
        selectOptionDTOs.add(new SelectOptionDTO("Boy Scout", "Boy Scout", false));
        return selectOptionDTOs;
    }
}

