package com.phonytive.callup.db.entities;

/**
 * Enum that map the ton type for an incoming call.
 *
 * @since 1.0.0
 */
public enum SubscriberStatus {

    ACTIVE("active"),
    INACTIVE("inactive"),
    REMOVED("removed");
    /**
     * CampaignStatus value.
     */
    private String value;

    /**
     * Create a new CampaignStatus object with value.
     * This class is an enum, therefore can't be instantiated directly.
     */
    private SubscriberStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
