package com.nixmash.springdata.jpa.repository.addons;

import com.nixmash.springdata.jpa.model.addons.Flashcard;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by daveburke on 8/11/16.
 */
public interface FlashcardRepository extends CrudRepository<Flashcard, Long> {
    List<Flashcard> findAll();

    List<Flashcard> findByCategoryId(long categoryId, Sort sort);

    @Query(value = "SELECT distinct f from Flashcard f left join fetch " +
            "f.category c left join fetch f.post p")
    List<Flashcard> findAllWithDetail();

    @Query(value = "SELECT distinct f from Flashcard f left join fetch " +
            "f.category c left join fetch f.post p where p.postId > 0 and c.categoryId = ?1")
    List<Flashcard> findActiveWithDetail(long categoryId);

}