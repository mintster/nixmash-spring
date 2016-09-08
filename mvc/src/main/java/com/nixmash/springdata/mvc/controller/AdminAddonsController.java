package com.nixmash.springdata.mvc.controller;

import com.nixmash.springdata.jpa.dto.addons.FlashcardCategoryDTO;
import com.nixmash.springdata.jpa.dto.addons.FlashcardDTO;
import com.nixmash.springdata.jpa.dto.addons.FlashcardImageDTO;
import com.nixmash.springdata.jpa.model.addons.Flashcard;
import com.nixmash.springdata.jpa.model.addons.FlashcardCategory;
import com.nixmash.springdata.jpa.service.AddonService;
import com.nixmash.springdata.mvc.components.WebUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/admin/addons")
public class AdminAddonsController {

    // region View Constants and redirects

    public static final String ADMIN_FLASHCARDS_VIEW = "admin/addons/flashcards/index";
    public static final String ADMIN_FLASHCARDS_ADD_VIEW = "admin/addons/flashcards/add";
    public static final String ADMIN_FLASHCARD_CATEGORIES_VIEW = "admin/addons/flashcards/categories";

    public static final String REDIRECT_FLASHCARD_CATEGORIES = "redirect:/admin/addons/flashcards/categories";
    private static final String REDIRECT_FLASHCARD_ADD = "redirect:/admin/addons/flashcards/add";

    // endregion

    // region Feedback Message Constants
    private static final String FEEDBACK_MESSAGE_FLASHCARD_CATEGORY_ADDED = "feedback.flashcard.category.added";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_CATEGORY_ERROR = "feedback.flashcard.category.error";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARDS_IN_CATEGORY_EXIST = "feedback.flashcards.in.category.error";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_CATEGORY_DELETED = "feedback.flashcard.category.deleted";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_CATEGORY_UPDATED = "feedback.flashcard.category.updated";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_UPDATE_SUCCESS = "feedback.flashcard.update.success";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_UPDATE_ERROR = "feedback.flashcard.update.error";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_DELETE_SUCCESS = "feedback.flashcard.delete.success";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_DELETE_ERROR = "feedback.flashcard.delete.error";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_ADD_ERROR = "feedback.flashcard.add.error";
    private static final String FEEDBACK_MESSAGE_KEY_FLASHCARD_ADD_SUCCESS = "feedback.flashcard.add.success";

    // endregion

    private final AddonService addonService;
    private final WebUI webUI;

    @Autowired
    public AdminAddonsController(WebUI webUI, AddonService addonService) {
        this.addonService = addonService;
        this.webUI = webUI;
    }

    private static final Logger logger = LoggerFactory.getLogger(AdminAddonsController.class);

    // region Flashcards

    @RequestMapping(value = "/flashcards", method = GET)
    public ModelAndView flashcardsMainPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("flashcards", addonService.getFlashcardsWithCategoryName());
        mav.addObject("flashcardCategories", addonService.getFlashcardCategories());
        mav.addObject("flashcardPosts", addonService.getFlashcardPosts());
        mav.setViewName(ADMIN_FLASHCARDS_VIEW);
        return mav;
    }

    @RequestMapping(value = "/flashcards/update/{slideId}", method = RequestMethod.POST)
    public String updateFlashcard(@Valid FlashcardDTO flashcard, BindingResult result,
                                  RedirectAttributes attributes) {
        if (result.hasErrors()) {
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_UPDATE_ERROR);
            return "redirect:/admin/addons/flashcards";
        } else {
            addonService.updateFlashcard(flashcard);
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_UPDATE_SUCCESS);
            return "redirect:/admin/addons/flashcards";
        }
    }

    @RequestMapping(value = "/flashcards/update/{slideId}", params = {"deleteFlashcard"}, method = RequestMethod.POST)
    public String deleteFlashcard(@Valid Flashcard flashcard, BindingResult result,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_DELETE_ERROR);
        } else {
            addonService.deleteFlashcard(flashcard);
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_DELETE_SUCCESS);
        }

        return "redirect:/admin/addons/flashcards";
    }

    @RequestMapping(value = "/flashcards/categories/new", method = RequestMethod.POST)
    public String addFlashcardCategory(@Valid FlashcardCategoryDTO flashcardCategoryDTO,
                                       BindingResult result,
                                       SessionStatus status,
                                       RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return ADMIN_FLASHCARD_CATEGORIES_VIEW;
        } else {

            FlashcardCategory flashcardCategory = addonService.addFlashCardCategory(new FlashcardCategory(flashcardCategoryDTO.getCategory()));
            logger.info("Flashcard Category Added: {}", flashcardCategory);
            status.setComplete();

            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_FLASHCARD_CATEGORY_ADDED, flashcardCategoryDTO.getCategory());
            return REDIRECT_FLASHCARD_CATEGORIES;
        }
    }

    @RequestMapping(value = "/flashcards/categories", method = GET)
    public ModelAndView flashcardCategories() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("flashcardCategories", addonService.getFlashcardCategories());
        mav.addObject("newCategory", new FlashcardCategory());
        mav.setViewName(ADMIN_FLASHCARD_CATEGORIES_VIEW);
        return mav;
    }

    @RequestMapping(value = "/flashcards/categories/update/{categoryId}", method = RequestMethod.POST)
    public String updateRole(@Valid @ModelAttribute(value = "flashcardCategory") FlashcardCategoryDTO flashcardCategoryDTO, BindingResult result,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_CATEGORY_ERROR);
            return REDIRECT_FLASHCARD_CATEGORIES;
        } else {
            addonService.updateFlashcardCategory(flashcardCategoryDTO);
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_CATEGORY_UPDATED, flashcardCategoryDTO.getCategory());
            return REDIRECT_FLASHCARD_CATEGORIES;
        }
    }

    @RequestMapping(value = "/flashcards/categories/update/{categoryId}", params = {"deleteCategory"}, method = RequestMethod.POST)
    public String deleteRole(@Valid @ModelAttribute(value = "flashcardCategory") FlashcardCategoryDTO flashcardCategoryDTO, BindingResult result,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_CATEGORY_ERROR);
            return REDIRECT_FLASHCARD_CATEGORIES;
        } else {
            FlashcardCategory flashcardCategory = addonService.getFlashcardCategoryById(flashcardCategoryDTO.getCategoryId());

            List<Flashcard> flashcards = addonService.getFlashcardsByCategoryId(flashcardCategoryDTO.getCategoryId());
            if (!flashcards.isEmpty()) {
                webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARDS_IN_CATEGORY_EXIST);
                return REDIRECT_FLASHCARD_CATEGORIES;
            }
            addonService.deleteFlashcardCategory(flashcardCategory);
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_CATEGORY_DELETED);
        }

        return REDIRECT_FLASHCARD_CATEGORIES;
    }

    @RequestMapping(value = "/flashcards/add", method = GET)
    public ModelAndView addFlashcard() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("flashcardCategories", addonService.getFlashcardCategories());
        mav.addObject("newFlashcard", new FlashcardImageDTO());
        mav.setViewName(ADMIN_FLASHCARDS_ADD_VIEW);
        return mav;
    }

    @RequestMapping(value = "/flashcards/add", method = RequestMethod.POST)
    public String handleFlashcardUpload(@Valid FlashcardImageDTO flashcardImageDTO,
                                   BindingResult result, RedirectAttributes attributes) throws IOException {

        if (result.hasErrors()) {
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_ADD_ERROR);
        } else {
            String filenameBase = UUID.randomUUID().toString();
            webUI.processFlashcardImage(flashcardImageDTO, filenameBase);
            flashcardImageDTO.setImage(filenameBase);
            addonService.addFlashcard(flashcardImageDTO);
            webUI.addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_FLASHCARD_ADD_SUCCESS);
        }
        return REDIRECT_FLASHCARD_ADD;
    }
}

// endregion

