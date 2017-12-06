package com.example.nmagen.usesdkexample.data;

import com.MobileTornado.sdk.model.data.Group;


/**
 * Created by nmagen on 29/11/2017.
 */

public class AppGroup {
    private Group group;
    private boolean isSelected;

    public AppGroup(Group g) {
        group = g;
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected() {
        isSelected = true;
    }

    public void setUnSelected() {
        isSelected = false;
    }

    public void setGroup(Group g) {
        group = g;
    }

    public Group getGroup() {
        return group;
    }
}
