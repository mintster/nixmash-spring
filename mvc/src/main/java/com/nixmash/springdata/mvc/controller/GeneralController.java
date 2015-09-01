package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.exceptions.UnknownResourceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class GeneralController {

    public static final String HOME_VIEW = "home";
    public static final String ERROR_403_VIEW = "errors/403";

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
}
