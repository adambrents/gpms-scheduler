package org.scheduler.viewmodels;

public class Contact {
    private final String contactName;
    private String contactEmail;
    private int contactId;

    public Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }
}
