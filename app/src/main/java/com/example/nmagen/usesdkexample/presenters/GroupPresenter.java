package com.example.nmagen.usesdkexample.presenters;

import com.MobileTornado.sdk.TornadoClient;
import com.MobileTornado.sdk.model.ContactsModule;
import com.MobileTornado.sdk.model.data.Group;
import com.example.nmagen.usesdkexample.data.AppGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nmagen on 28/11/2017.
 */

public class GroupPresenter {
    private ContactsModule contactsModule = TornadoClient.getInstance().getContactsModule();
    private List<Group> groupList = contactsModule.getGroups();
    private List<AppGroup> appGroupList = new ArrayList<AppGroup>();
    private int listSize = groupList.size();

    public GroupPresenter() {
        for (int i = 0; i < listSize; i++) {
            AppGroup ag = new AppGroup(groupList.get(i));
            appGroupList.add(ag);
        }
    }

    public List<AppGroup> getGroupList() {
        return appGroupList;
    }
}
