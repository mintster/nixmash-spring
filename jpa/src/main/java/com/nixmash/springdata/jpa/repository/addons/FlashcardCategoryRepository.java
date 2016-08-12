package com.nixmash.springdata.jpa.repository.addons;

import com.nixmash.springdata.jpa.model.addons.FlashcardCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by daveburke on 8/11/16.
 */
public interface FlashcardCategoryRepository extends CrudRepository<FlashcardCategory, Long> {
    List<FlashcardCategory> findAll();

    FlashcardCategory findByCategoryId(long categoryId);
}
