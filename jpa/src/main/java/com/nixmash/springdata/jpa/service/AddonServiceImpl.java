package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.common.ApplicationSettings;
import com.nixmash.springdata.jpa.dto.addons.FlashcardCategoryDTO;
import com.nixmash.springdata.jpa.dto.addons.FlashcardDTO;
import com.nixmash.springdata.jpa.dto.addons.FlashcardImageDTO;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.model.addons.Flashcard;
import com.nixmash.springdata.jpa.model.addons.FlashcardCategory;
import com.nixmash.springdata.jpa.repository.PostRepository;
import com.nixmash.springdata.jpa.repository.addons.FlashcardCategoryRepository;
import com.nixmash.springdata.jpa.repository.addons.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by daveburke on 8/11/16.
 */
@Service("addonService")
@Transactional
public class AddonServiceImpl implements AddonService {

    private final FlashcardCategoryRepository flashcardCategoryRepository;
    private final FlashcardRepository flashcardRepository;
    private final PostRepository postRepository;
    private final ApplicationSettings applicationSettings;

    @Autowired
    public AddonServiceImpl(FlashcardCategoryRepository flashcardCategoryRepository, FlashcardRepository flashcardRepository, PostRepository postRepository, ApplicationSettings applicationSettings) {
        this.flashcardCategoryRepository = flashcardCategoryRepository;
        this.flashcardRepository = flashcardRepository;
        this.postRepository = postRepository;
        this.applicationSettings = applicationSettings;
    }

    @PersistenceContext
    private EntityManager em;

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
    public Flashcard addFlashcard(FlashcardImageDTO flashcardImageDTO) {
        Post post = postRepository.findOne(flashcardImageDTO.getPostId());
        Flashcard flashcard = new Flashcard(flashcardImageDTO.getCategoryId(),
                                                                                                flashcardImageDTO.getImage(),
                                                                                                flashcardImageDTO.getContent(),
                                                                                                post);
        return flashcardRepository.save(flashcard);
    }

    @Override
    public List<Flashcard> getAllFlashcards() {
        return flashcardRepository.findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Flashcard> getFlashcardsWithCategoryName() {
        List<Flashcard> flashcards =
                em.createNativeQuery("SELECT *  FROM v_flashcards", "FlashcardsWithCategory")
                .getResultList();

        String imageRootUrl = applicationSettings.getFlashcardImageUrlRoot();
        for (Flashcard flashcard : flashcards) {
            flashcard.setImageUrl(String.format("%s%s.png", imageRootUrl, flashcard.getImage()));
            flashcard.setThumbnailUrl(String.format("%s%s-thumbnail.png", imageRootUrl, flashcard.getImage()));
        }
        return flashcards;
    }

    @Override
    public Flashcard updateFlashcard(FlashcardDTO flashcardDTO) {
        Flashcard found = flashcardRepository.findOne(flashcardDTO.getSlideId());
        Post post = postRepository.findOne(flashcardDTO.getPostId());
        found.update(flashcardDTO.getCategoryId(), flashcardDTO.getContent(), post);
        return found;
    }

    @Override
    public void deleteFlashcard(Flashcard flashcard) {
        flashcardRepository.delete(flashcard);
    }

    @Override
    public List<Flashcard> getFlashcardsWithDetail() {
        return flashcardRepository.findAllWithDetail();
    }

    @Override
    public List<Post> getFlashcardPosts() {
        return postRepository.findSinglePhotoPosts();
    }

    // endregion

}
