package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.addons.FlashcardCategoryDTO;
import com.nixmash.springdata.jpa.dto.addons.FlashcardDTO;
import com.nixmash.springdata.jpa.dto.addons.FlashcardImageDTO;
import com.nixmash.springdata.jpa.model.Post;
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

    Flashcard addFlashcard(FlashcardImageDTO flashcardImageDTO);

    List<Flashcard> getAllFlashcards();

    List<Flashcard> getFlashcardsWithCategoryName();

    @Transactional
    Post getFlashcardPost(List<Flashcard> flashcards, int index);

    Flashcard updateFlashcard(FlashcardDTO flashcardDTO);

    void deleteFlashcard(Flashcard flashcard);

    List<Flashcard> getFlashcardsWithDetail();

    List<Flashcard> getActiveFlashcardsWithDetail(long categoryId);

    List<Post> getFlashcardPosts();
}
