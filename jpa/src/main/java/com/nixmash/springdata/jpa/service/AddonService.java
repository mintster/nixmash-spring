package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.addons.FlashcardCategoryDTO;
import com.nixmash.springdata.jpa.model.addons.Flashcard;
import com.nixmash.springdata.jpa.model.addons.FlashcardCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by daveburke on 8/11/16.
 */
public interface AddonService {
    FlashcardCategory getFlashcardCategoryById(long categoryId);

    List<FlashcardCategory> getFlashcardCategories();

    @Transactional
    void deleteFlashcardCategory(FlashcardCategory flashcardCategory);

    @Transactional
    FlashcardCategory updateFlashcardCategory(FlashcardCategoryDTO flashcardCategoryDTO);

    List<Flashcard> getFlashcardsByCategoryId(long categoryId);

    FlashcardCategory addFlashCardCategory(FlashcardCategory flashcardCategory);

    Flashcard addFlashcard(Flashcard flashcard);

    List<Flashcard> getAllFlashcards();

    List<Flashcard> getFlashcardsWithCategoryName();

    Flashcard updateFlashcard(Flashcard flashcard);

    void deleteFlashcard(Flashcard flashcard);
}
