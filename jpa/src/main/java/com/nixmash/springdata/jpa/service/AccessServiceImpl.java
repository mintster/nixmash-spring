package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.AccessDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by daveburke on 12/10/16.
 */
@Service("accessService")
@PropertySource("file:${external.properties.file.path}access.properties")
public class AccessServiceImpl implements AccessService {

    private static final Logger logger = LoggerFactory.getLogger(AccessServiceImpl.class);

    @Value("${access.blacklisted.email.endswith}")
    String blackListedDomains;

    @Value("${access.blacklisted.email.overrides}")
    String overrideDomains;

    @Override
    public boolean isEmailApproved(String email) {
        AccessDTO accessDTO = createAccessDTO(email);
        return accessDTO.isApproved();
    }

    @Override
    public AccessDTO createAccessDTO(String email) {

        AccessDTO accessDTO = getEmailDomain(email);
        if (accessDTO.isValid()) {
            String domain = accessDTO.getDomain();
            int startIndex = 0;
            int nextIndex = domain.indexOf('.');
            int lastIndex = domain.lastIndexOf('.');
            while (nextIndex < lastIndex) {
                startIndex = nextIndex + 1;
                nextIndex = domain.indexOf('.', startIndex);
            }
            if (startIndex > 0) {
                accessDTO.setDomain(domain.substring(startIndex));
            } else {
                accessDTO.setDomain(domain);
            }
            accessDTO.setApproved(!isDomainBlacklisted(accessDTO.getDomain()));
        }
        logger.debug(String.valueOf(accessDTO));
        return accessDTO;
    }

    private AccessDTO getEmailDomain(String email) {
        AccessDTO accessDTO = new AccessDTO(email);
        int ampersand = email.indexOf("@");
        if (ampersand > 0) {
            accessDTO.setDomain(email.substring(ampersand + 1));
            if (accessDTO.getDomain().contains("."))
                accessDTO.setValid(true);
        }
        return accessDTO;
    }

    private List<String> blackListedDomains() {
        return Arrays.asList(blackListedDomains.split(","));
    }

    private List<String> overrideDomains() {
        return Arrays.asList(overrideDomains.split(","));
    }

    private boolean isDomainBlacklisted(String domain) {
        boolean isBlacklisted = false;
        for (String blacklistedDomain :
                blackListedDomains()) {
            if (domain.endsWith(blacklistedDomain))
                isBlacklisted = true;
        }
        for (String overrideDomain :
                overrideDomains()) {
            if (domain.endsWith(overrideDomain))
                isBlacklisted = false;
        }
        return isBlacklisted;
    }


}
