package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.enums.SiteOptionType;
import com.nixmash.springdata.jpa.repository.SiteOptionRepository;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class SiteOptionTests {

    @Autowired
    SiteOptionRepository siteOptionRepository;

    @Autowired
    SiteOptions siteOptionsUI;

    @Test
    public void retrieveSiteOptions() {

        Collection<SiteOption> siteOptions = siteOptionRepository.findAll();
        assertNotNull(siteOptions);

        Dictionary options = new Hashtable();
        for (SiteOption siteOption : siteOptions) {
            options.put(siteOption.getName(), siteOption.getValue());
        }

        Enumeration<String> e = options.keys();

        assertNull(siteOptionsUI.getAddGoogleAnalytics());

        while (e.hasMoreElements()) {
            String key = e.nextElement();
            for (Field f : siteOptionsUI.getClass().getDeclaredFields()) {
                if (f.getName().equals(key)) {
                    try {
                        attemptToSetProperty(siteOptionsUI, key, options.get(key));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        assert (siteOptionsUI.getAddGoogleAnalytics().equals(false));
        assert (siteOptionsUI.getIntegerProperty().equals(1));
        assert (siteOptionsUI.getGoogleAnalyticsTrackingId().equals("UA-XXXXXX-7"));

    }

    private boolean attemptToSetProperty(Object bean, String property, Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (PropertyUtils.isWriteable(bean, property)) {

            switch (PropertyUtils.getPropertyDescriptor(bean, property).getPropertyType().getSimpleName()) {
                case SiteOptionType.BOOLEAN:
                    value = Boolean.valueOf(value.toString());
                    break;
                case SiteOptionType.INTEGER:
                    value = Integer.parseInt(value.toString());
                    break;
                default:
                    break;
            }
            PropertyUtils.setProperty(bean, property, value);
            return true;
        }
        return false;
    }

}
