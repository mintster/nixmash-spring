package com.nixmash.springdata.jpa.model.auditors;

import java.time.ZonedDateTime;

/**
 * This interface defines the methods used to with the current
 * date and time.
 *
 * From Petri Kainulainen's JPA Examples Project on GitHub
 *
 * spring-data-jpa-examples/query-methods/
 * https://goo.gl/lY7sT5
 *
 */

public interface DateTimeService {

    /**
     * Returns the current date and time.
     * @return
     */
    ZonedDateTime getCurrentDateAndTime();
}
