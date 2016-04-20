
package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.common.ApplicationContextUtils;
import com.nixmash.springdata.jpa.common.ApplicationSettings;
import org.springframework.context.ApplicationContext;

public class CurrentUser
        extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 7828419298616811182L;

    private User user;

    public CurrentUser(User user) {
        super(user.getUsername(), user.getPassword(),
                user.getAuthorities());
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getFullName() {
        return user.getFirstName() + ' ' + user.getLastName();
    }

    public Long getId() {
        return user.getId();
    }

    public String getProfileIconUrl() {

//        SpringBean bean = (SpringBean)SpringContext.getApplicationContext.getBean("springBean");
        ApplicationContext appContext = ApplicationContextUtils.getApplicationContext();
        ApplicationSettings applicationSettings = (ApplicationSettings) appContext.getBean("applicationSettings");

        String iconUrl = "/images/user32x32.png";
        if (this.user.hasAvatar()) {
            iconUrl = applicationSettings.getProfileIconUrlRoot() + user.getUserKey();
        }
        return iconUrl;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                '}' + super.toString();
    }
}