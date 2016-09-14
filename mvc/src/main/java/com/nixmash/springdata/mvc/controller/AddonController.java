package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.addons.Flashcard;
import com.nixmash.springdata.jpa.model.addons.FlashcardCategory;
import com.nixmash.springdata.jpa.service.AddonService;
import com.nixmash.springdata.mail.service.FmService;
import com.nixmash.springdata.mvc.components.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by daveburke on 8/23/16.
 */
@Controller
public class AddonController {

    private static final Logger logger = LoggerFactory.getLogger(AddonController.class);
    public static final String FLASHCARDS_VIEW = "posts/flashcards";
    private static final String SESSION_ATTRIBUTE_FLASHCARDS = "flashcards";
    private static final String FLASHCARD_POST_TEMPLATE = "flashcard_post";

    private final AddonService addonService;
    private final WebUI webUI;
    private final ApplicationSettings applicationSettings;
    private final FmService fmService;

    @Autowired
    public AddonController(AddonService addonService,
                           WebUI webUI,
                           ApplicationSettings applicationSettings,
                           FmService fmService) {
        this.addonService = addonService;
        this.webUI = webUI;
        this.applicationSettings = applicationSettings;
        this.fmService = fmService;
    }

    @RequestMapping(value = "/posts/flashcards", method = GET)
    public String getFlashcardsView(Model model, HttpServletRequest request) {
        long categoryId = applicationSettings.getDefaultFlashcardCategory();
        model.addAllAttributes(getFlashcardAttributes(categoryId, request));
        return FLASHCARDS_VIEW;
    }

    @RequestMapping(value = "/posts/flashcards", method = POST)
    public String getFlashcardsFromSelect(@ModelAttribute(value = "category")
                                                        FlashcardCategory category,
                                                        Model model, HttpServletRequest request) {
        model.addAllAttributes(getFlashcardAttributes(category.getCategoryId(), request));
        return FLASHCARDS_VIEW;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/posts/flashcards/answer/{index}",
                                                        produces = "text/html;charset=UTF-8")
    public @ResponseBody
    String showAnswer(@PathVariable int index, HttpServletRequest request) {
        List<Flashcard> flashcards =
                (List<Flashcard>) WebUtils.getSessionAttribute(request, SESSION_ATTRIBUTE_FLASHCARDS);

        if (flashcards != null) {
            Post post = addonService.getFlashcardPost(flashcards, index);
            return fmService.createPostHtml(post, FLASHCARD_POST_TEMPLATE);
        } else {
            return null;
        }
    }

    private Map<String, Object> getFlashcardAttributes(long categoryId, HttpServletRequest request) {
        List<Flashcard> flashcards = addonService.getActiveFlashcardsWithDetail(categoryId);
        List<FlashcardCategory> categories = addonService.getFlashcardCategories();
        FlashcardCategory currentCategory = addonService.getFlashcardCategoryById(categoryId);

        Map<String, Object> attributes = new HashMap<>();
        WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_FLASHCARDS, null);
        attributes.put("flashcards", flashcards);
        attributes.put("categories", categories);
        attributes.put("currentCategory", currentCategory);
        WebUtils.setSessionAttribute(request, SESSION_ATTRIBUTE_FLASHCARDS, flashcards);
        return attributes;
    }
}
