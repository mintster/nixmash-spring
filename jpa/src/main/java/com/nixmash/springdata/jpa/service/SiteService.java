package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.SiteOptionDTO;
import com.nixmash.springdata.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.springdata.jpa.model.SiteOption;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by daveburke on 5/7/16.
 */
public interface SiteService {

    SiteOption update(SiteOptionDTO siteOptionDTO) throws SiteOptionNotFoundException;

    @Transactional(readOnly = true)
    SiteOption findOptionByName(String name) throws SiteOptionNotFoundException;

}
