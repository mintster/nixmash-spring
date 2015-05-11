package com.nixmash.springdata.jpa.enums;

/**
 * Helper for Spring Config
 * <p/>
 * Source {@link org.hibernate.cfg.SettingsFactory}
 * <ul>
 * <li>"create"      - to build a new database on each run</li>
 * <li>"update"      - modify an existing database</li>
 * <li>"create-drop" - same as "create" and drops tables on Hibernate close</li>
 * <li>"validate"    - Validates entities against Schema, violations throw exceptions, no changes to the database</li>
 * <li>"none"        - What do you guess?</li>
 * </ul>
 *
 * @author Gordon Dickens
 * @see org.hibernate.cfg.SettingsFactory
 */
public enum Hbm2ddlType {
    CREATE("create"),
    UPDATE("update"),
    VALIDATE("validate"),
    CREATE_DROP("create-drop"),
    NONE("none");

    private final String value;

    Hbm2ddlType(final String type) {
        this.value = type;
    }

    public String toValue() {
        return value;
    }

    public static Hbm2ddlType fromValue(final String value) {
        if (value != null) {
            for (Hbm2ddlType hbm2ddl : values()) {
                if (hbm2ddl.value.equals(value)) {
                    return hbm2ddl;
                }
            }
        }
        return Hbm2ddlType.NONE;
    }

}
