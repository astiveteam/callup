package com.phonytive.callup.db.entities;

/**
 * Enum that map the ton type for an incoming call.
 *
 * @since 1.0.0
 */
public enum Day {

    MONDAY("monday"),
    THUESDAY("thuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday"),
    SUNDAY("sunday");
    
    /**
     * CampaignType value.
     */
    private String value;

    /**
     * Create a new Day object.
     * This class is an enum, therefore can't be instantiated directly.
     */
    private Day(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
