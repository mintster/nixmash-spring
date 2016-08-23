package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.model.addons.Flashcard;
import com.nixmash.springdata.jpa.model.addons.FlashcardCategory;
import com.nixmash.springdata.jpa.service.AddonService;
import com.nixmash.springdata.mvc.components.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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

    private final AddonService addonService;
    private final WebUI webUI;
    private final ApplicationSettings applicationSettings;

    @Autowired
    public AddonController(AddonService addonService, WebUI webUI, ApplicationSettings applicationSettings) {
        this.addonService = addonService;
        this.webUI = webUI;
        this.applicationSettings = applicationSettings;
    }

    @RequestMapping(value = "/posts/flashcards", method = GET)
    public String getFlashcardsView(Model model) {
        long categoryId = applicationSettings.getDefaultFlashcardCategory();
        model.addAllAttributes(getFlashcardAttributes(categoryId));
        return FLASHCARDS_VIEW;
    }

    @RequestMapping(value = "/posts/flashcards", method = POST)
    public String getFlashcardsFromSelect(@ModelAttribute(value = "category") FlashcardCategory category,
                                          Model model) {
        model.addAllAttributes(getFlashcardAttributes(category.getCategoryId()));
        return FLASHCARDS_VIEW;
    }


    private Map<String, Object> getFlashcardAttributes(long categoryId) {
        List<Flashcard> flashcards = addonService.getActiveFlashcardsWithDetail(categoryId);
        List<FlashcardCategory> categories = addonService.getFlashcardCategories();
        FlashcardCategory currentCategory = addonService.getFlashcardCategoryById(categoryId);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("flashcards", flashcards);
        attributes.put("categories", categories);
        attributes.put("currentCategory", currentCategory);
        return attributes;
    }
}
