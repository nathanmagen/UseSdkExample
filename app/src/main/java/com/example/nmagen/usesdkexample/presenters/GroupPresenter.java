package com.example.nmagen.usesdkexample.presenters;

import com.MobileTornado.sdk.TornadoClient;
import com.MobileTornado.sdk.model.ContactsModule;
import com.MobileTornado.sdk.model.data.Group;

import java.util.List;

/**
 * Created by nmagen on 28/11/2017.
 */

public class GroupPresenter {
    private ContactsModule contactsModule = TornadoClient.getInstance().getContactsModule();
    private List<Group> groupList = contactsModule.getGroups();

    public List<Group> getGroupList() {
        return groupList;
    }
}
