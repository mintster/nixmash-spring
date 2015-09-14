package com.nixmash.springdata.jpa.model.auditors;

import org.springframework.data.auditing.DateTimeProvider;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class obtains the current time by using a {@link DateTimeService}
 * object. The reason for this is that we can use a different implementation in our integration tests.
 *
 * Uses {@link CurrentTimeDateTimeService} class.
 *
 * From Petri Kainulainen's JPA Examples Project on GitHub
 *
 * spring-data-jpa-examples/query-methods/
 * https://goo.gl/lY7sT5
 *
 */
public class AuditingDateTimeProvider implements DateTimeProvider {

    private final DateTimeService dateTimeService;

    public AuditingDateTimeProvider(DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }

    @Override
    public Calendar getNow() {
        return GregorianCalendar.from(dateTimeService.getCurrentDateAndTime());
    }
}
