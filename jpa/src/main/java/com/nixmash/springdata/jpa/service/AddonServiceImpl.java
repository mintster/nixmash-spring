package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.addons.FlashcardCategoryDTO;
import com.nixmash.springdata.jpa.model.addons.Flashcard;
import com.nixmash.springdata.jpa.model.addons.FlashcardCategory;
import com.nixmash.springdata.jpa.repository.addons.FlashcardCategoryRepository;
import com.nixmash.springdata.jpa.repository.addons.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by daveburke on 8/11/16.
 */
@Service("addonService")
@Transactional
public class AddonServiceImpl implements AddonService {

    private final FlashcardCategoryRepository flashcardCategoryRepository;
    private final FlashcardRepository flashcardRepository;

    @Autowired
    public AddonServiceImpl(FlashcardCategoryRepository flashcardCategoryRepository, FlashcardRepository flashcardRepository) {
        this.flashcardCategoryRepository = flashcardCategoryRepository;
        this.flashcardRepository = flashcardRepository;
    }

    // region Flashcards

    @Override
    public FlashcardCategory getFlashcardCategoryById(long categoryId) {
        return flashcardCategoryRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<FlashcardCategory> getFlashcardCategories() {
        return flashcardCategoryRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteFlashcardCategory(FlashcardCategory flashcardCategory) {
        flashcardCategoryRepository.delete(flashcardCategory);
    }

    @Transactional
    @Override
    public FlashcardCategory updateFlashcardCategory(FlashcardCategoryDTO flashcardCategoryDTO) {
        FlashcardCategory flashcardCategory = flashcardCategoryRepository.findOne(flashcardCategoryDTO.getCategoryId());
        flashcardCategory.setCategory(flashcardCategoryDTO.getCategory());
        return flashcardCategory;
    }

    @Override
    public List<Flashcard> getFlashcardsByCategoryId(long categoryId) {
        return flashcardRepository.findByCategoryId(categoryId);
    }

    @Override
    public FlashcardCategory addFlashCardCategory(FlashcardCategory flashcardCategory) {
        return flashcardCategoryRepository.save(flashcardCategory);
    }

    @Override
    public Flashcard addFlashcard(Flashcard flashcard) {
        return flashcardRepository.save(flashcard);
    }

    @Override
    public List<Flashcard> getAllFlashcards() {
        return flashcardRepository.findAll();
    }
    // endregion

}
