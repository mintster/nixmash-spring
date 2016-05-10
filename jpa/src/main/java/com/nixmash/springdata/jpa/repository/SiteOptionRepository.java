package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.SiteOption;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;

import java.util.Collection;

/**
 * Created by daveburke on 5/7/16.
 */
public interface SiteOptionRepository extends Repository<SiteOption, Long> {

    SiteOption findByNameIgnoreCase(String optionName) throws DataAccessException;

    Collection<SiteOption> findAll() throws DataAccessException;

    SiteOption save(SiteOption siteOption) throws DataAccessException;

}