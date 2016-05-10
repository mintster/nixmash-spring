package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.SiteOptionDTO;
import com.nixmash.springdata.jpa.exceptions.SiteOptionNotFoundException;
import com.nixmash.springdata.jpa.model.SiteOption;
import com.nixmash.springdata.jpa.repository.SiteOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("siteService")
@Transactional
public class SiteServiceImpl implements SiteService{

    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

    SiteOptionRepository siteOptionRepository;

    @Autowired
    public SiteServiceImpl(SiteOptionRepository siteOptionRepository) {
        this.siteOptionRepository = siteOptionRepository;
    }

    @Override
    public SiteOption update(SiteOptionDTO siteOptionDTO) throws SiteOptionNotFoundException {
        logger.info("Updating siteOption property {} with value: {}", siteOptionDTO.getName(), siteOptionDTO.getValue());
        SiteOption found = findOptionByName(siteOptionDTO.getName());
        found.update(siteOptionDTO.getName(), siteOptionDTO.getValue());
        return found;
    }

    @Transactional(readOnly = true)
    @Override
    public SiteOption findOptionByName(String name) throws SiteOptionNotFoundException {

        logger.info("Finding siteOption property with name: {}", name);
        SiteOption found = siteOptionRepository.findByNameIgnoreCase(name);

        if (found == null) {
            logger.info("No siteOption property with name: {}", name);
            throw new SiteOptionNotFoundException("No siteOption with property name: " + name);
        }

        return found;
    }
}



