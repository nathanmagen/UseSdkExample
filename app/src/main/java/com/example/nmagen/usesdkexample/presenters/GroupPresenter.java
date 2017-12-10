package com.example.nmagen.usesdkexample.presenters;

import com.MobileTornado.sdk.TornadoClient;
import com.MobileTornado.sdk.model.ContactsModule;
import com.MobileTornado.sdk.model.data.Contact;
import com.MobileTornado.sdk.model.data.Group;
import com.example.nmagen.usesdkexample.activities.AddGroupActivity;
import com.example.nmagen.usesdkexample.data.AppContact;
import com.example.nmagen.usesdkexample.data.AppGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nmagen on 28/11/2017.
 */

public class GroupPresenter {
    private ContactsModule contactsModule = TornadoClient.getInstance().getContactsModule();
    private List<Group> groupList;
    private List<Contact> contactList;
    private List<AppGroup> appGroupList = new ArrayList<AppGroup>();
    private List<AppContact> appContactList = new ArrayList<>();

    public GroupPresenter() {
        refreshGroups();
        refreshContacts();
    }

    public void refreshGroups() {
        groupList = contactsModule.getGroups();
        int listSize = groupList.size();
        if (appGroupList.size() > 0) {
            appGroupList.clear();
        }

        for (int i = 0; i < listSize; i++) {
            AppGroup ag = new AppGroup(groupList.get(i));
            appGroupList.add(ag);
        }
    }

    public void refreshContacts() {
        contactList = contactsModule.getContacts();
        int listSize = contactList.size();
        if (appContactList.size() > 0) {
            appContactList.clear();
        }

        for (int i = 0; i < listSize; i++) {
            AppContact ac = new AppContact(contactList.get(i));
            appContactList.add(ac);
        }
    }

    public void addGroupAddListener(ContactsModule.GroupAddListener gaListener) {
        contactsModule.addGroupAddListener(gaListener);
    }

    public void addGroupRemoveListener(ContactsModule.GroupRemoveListener removeListener) {
        contactsModule.addGroupRemoveListener(removeListener);
    }

    public void addGroup(String groupName) {
        contactsModule.addGroup(groupName);
        refreshGroups();
    }

    public void addContactToGroup(long contactId, long groupId) {
        contactsModule.addContactToGroup(contactId, groupId);
    }

    public List<AppGroup> getGroupList() {
        return appGroupList;
    }

    public List<AppContact> getContacts() {
        return appContactList;
    }

    public AppGroup getGroupByName(String groupName) {
        int size = appGroupList.size();
        for (int i = 0; i < size; i++) {
            AppGroup candidateGroup = appGroupList.get(i);
            if (groupName.matches(candidateGroup.getGroup().getDisplayName())) {
                return candidateGroup;
            }
        }
        return null;
    }

    public void removeGroup(long groupId) {
        contactsModule.removeGroup(groupId);
    }
}
