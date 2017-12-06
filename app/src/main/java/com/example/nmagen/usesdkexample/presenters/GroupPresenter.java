package com.example.nmagen.usesdkexample.presenters;

import com.MobileTornado.sdk.TornadoClient;
import com.MobileTornado.sdk.model.ContactsModule;
import com.MobileTornado.sdk.model.data.Contact;
import com.MobileTornado.sdk.model.data.Group;
import com.example.nmagen.usesdkexample.data.AppContact;
import com.example.nmagen.usesdkexample.data.AppGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nmagen on 28/11/2017.
 */

public class GroupPresenter {
    private ContactsModule contactsModule = TornadoClient.getInstance().getContactsModule();
    private List<Group> groupList = contactsModule.getGroups();
    private List<Contact> contactList = contactsModule.getContacts();
    private List<AppGroup> appGroupList = new ArrayList<AppGroup>();
    private List<AppContact> appContactList = new ArrayList<>();

    public GroupPresenter() {
        refreshGroups();
        refreshContacts();
    }

    public void refreshGroups() {
        int listSize = groupList.size();
        for (int i = 0; i < listSize; i++) {
            AppGroup ag = new AppGroup(groupList.get(i));
            appGroupList.add(ag);
        }
    }

    public void refreshContacts() {
        int listSize = contactList.size();
        for (int i = 0; i < listSize; i++) {
            AppContact ac = new AppContact(contactList.get(i));
            appContactList.add(ac);
        }
    }

    public List<AppGroup> getGroupList() {
        return appGroupList;
    }

    public List<AppContact> getContacts() {
        return appContactList;
    }
}
