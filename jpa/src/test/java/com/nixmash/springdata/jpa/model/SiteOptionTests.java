package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.common.SiteOptions;
import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.repository.SiteOptionRepository;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import static com.nixmash.springdata.jpa.common.SiteOptions.OPTION_VALUE_TYPE_BOOLEAN;
import static com.nixmash.springdata.jpa.common.SiteOptions.OPTION_VALUE_TYPE_INTEGER;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@ActiveProfiles(DataConfigProfile.H2)
public class SiteOptionTests {

    @Autowired
    protected ApplicationContext context;

    @Autowired
    SiteOptionRepository siteOptionRepository;

    @Autowired
    SiteOptions siteOptions;

//    @Before
//    public void setup() {
//        contextLoads();
//    }
//
//    @Test
//    public void contextLoads() {
//    }

//    @Test
//    public void siteOptionsIsPopulatedFromContext() {
//        assertNotNull(siteOptions.getGoogleAnalyticsTrackingId());
//    }

    @Test
    public void canPopulateSiteOptionsFromKeyValueData() throws
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Collection<SiteOption> siteOptionKeyValues = siteOptionRepository.findAll();
        assertNotNull(siteOptionKeyValues);

        Map<String, Object> options = new Hashtable<>();
        for (SiteOption siteOption : siteOptionKeyValues) {
            options.put(siteOption.getName(), siteOption.getValue());
        }

        assertNull(siteOptions.getAddGoogleAnalytics());

        for (String key : options.keySet()) {
            for (Field f : siteOptions.getClass().getDeclaredFields()) {
                if (f.getName().toUpperCase().equals(key.toUpperCase())) {
                    setSiteOptionProperty(key, options.get(key));
                }
            }
        }

        assert (siteOptions.getAddGoogleAnalytics().equals(false));
        assert (siteOptions.getIntegerProperty().equals(1));
        assert (siteOptions.getGoogleAnalyticsTrackingId().equals("UA-XXXXXX-7"));
        assert (siteOptions.getSiteName().equals("My Site"));
        assert (siteOptions.getSiteDescription().equals("My Site Description"));

    }

    private void setSiteOptionProperty(String property, Object value)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (PropertyUtils.isWriteable(siteOptions, property)) {

            switch (PropertyUtils
                    .getPropertyDescriptor(siteOptions, property).getPropertyType().getSimpleName()) {
                case OPTION_VALUE_TYPE_BOOLEAN:
                    value = Boolean.valueOf(value.toString());
                    break;
                case OPTION_VALUE_TYPE_INTEGER:
                    value = Integer.parseInt(value.toString());
                    break;
                default:
                    break;
            }
            PropertyUtils.setProperty(siteOptions, property, value);
        }
    }

    private void printSiteOptionsBeanProperties() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PropertyDescriptor[] pds;
        System.out.println("\n\nGet properties of SiteOptions Bean");
        pds = PropertyUtils.getPropertyDescriptors(siteOptions);
        for (PropertyDescriptor pd : pds) {
            System.out.println("property name: " + pd.getName());
            System.out.println("property display name: " + pd.getDisplayName());
            System.out.println("property type: " + pd.getPropertyType());
            System.out.println("property value: " + PropertyUtils.getSimpleProperty(siteOptions, pd.getName()));
            System.out.println();
        }
    }
}
