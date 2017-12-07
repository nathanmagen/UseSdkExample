package com.example.nmagen.usesdkexample.data;

import com.MobileTornado.sdk.model.data.Contact;

/**
 * Created by nmagen on 06/12/2017.
 */

public class AppContact {
    private Contact contact;
    private boolean isSelected;
    private boolean isAdded;

    public AppContact(Contact c) {
        contact = c;
        isSelected = false;
        isAdded = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSel) {
        isSelected = isSel;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setIsAdded(boolean isAd) {
        isAdded = isAd;
    }

    public Contact getContact() {
        return contact;
    }
}
