package com.nixmash.springdata.jpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("siteService")
@Transactional
public class SiteServiceImpl implements SiteService{

    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

//    SiteOptions siteOptions;
//    SiteOptionRepository siteOptionRepository;
//
//    @Autowired
//    public SiteServiceImpl(SiteOptions siteOptions, SiteOptionRepository siteOptionRepository) {
//        this.siteOptions = siteOptions;
//        this.siteOptionRepository = siteOptionRepository;
//    }


}



