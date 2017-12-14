package com.example.nmagen.usesdkexample.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.MobileTornado.sdk.model.ContactsModule;
import com.MobileTornado.sdk.model.data.Contact;
import com.MobileTornado.sdk.model.data.Group;
import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.adapters.ListToViewAdapter;
import com.example.nmagen.usesdkexample.data.AppContact;
import com.example.nmagen.usesdkexample.data.AppGroup;
import com.example.nmagen.usesdkexample.listeners.RecyclerTouchListener;
import com.example.nmagen.usesdkexample.presenters.GroupPresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;

import java.util.ArrayList;
import java.util.List;

public class AddGroupActivity extends AppCompatActivity {
    private PresentersManager presentersManager = PresentersManager.getInstance();
    private GroupPresenter groupPresenter = presentersManager.getGroupPresenter();
    private List<AppContact> contactsList = groupPresenter.getContacts();
    private List<String> contactsNameList = new ArrayList<>();
    private List<AppContact> contactsToAdd = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public int clickedOnContactPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        groupPresenter.addGroupAddListener(new ContactsModule.GroupAddListener() {
            @Override
            public void onGroupAdded(Group group) {
                Toast.makeText(getApplicationContext(), group.getDisplayName() + " added successfully, Please add contacts", Toast.LENGTH_LONG).show();
            }
        });

        // Setting the recyclerView
        recyclerView = findViewById(R.id.contacts_list_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        fillNameList();
        adapter = new ListToViewAdapter(this, contactsNameList, R.layout.contact_list_line, R.id.contact_name_view, R.id.add_button);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickedOnContactPosition = position;
                AppContact clickedContact = contactsList.get(position);
                if (!clickedContact.isSelected()) {
                    // select procedure
                    clickedContact.setIsSelected(true);
                    if (clickedContact.isAdded()) {
                        view.findViewById(R.id.add_button).setEnabled(false);
                        view.findViewById(R.id.remove_button).setEnabled(true);
                    }
                    else {
                        view.findViewById(R.id.add_button).setEnabled(true);
                        view.findViewById(R.id.remove_button).setEnabled(false);
                    }
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

                // unselecting all the rest of the groups in the list
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisiblePos = linearLayoutManager.findFirstVisibleItemPosition();
                int itemCount = linearLayoutManager.findLastVisibleItemPosition() - firstVisiblePos + 1;
                int newPos = position - firstVisiblePos;
                for (int i = 0; i < itemCount; i++) {
                    if (newPos != i) {
                        contactsList.get(i + firstVisiblePos).setIsSelected(false);
                        View v = recyclerView.getChildAt(i);
                        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        v.findViewById(R.id.add_button).setEnabled(false);
                        v.findViewById(R.id.remove_button).setEnabled(false);
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ));
    }

    public void onClickAdd(View view) {
        int pos = (int)view.getTag();
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int newPos = pos - linearLayoutManager.findFirstVisibleItemPosition();
        View v = recyclerView.getChildAt(newPos);
        Button removeButt = v.findViewById(R.id.remove_button);
        removeButt.setTag(pos);
        removeButt.setEnabled(true);
        view.setEnabled(false);
        AppContact contactToAdd = contactsList.get(pos);
        contactToAdd.setIsAdded(true);
        contactsToAdd.add(contactToAdd);
    }

    public void onClickRemove(View view) {
        int pos = (int)view.getTag();
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int newPos = pos - linearLayoutManager.findFirstVisibleItemPosition();
        View v = recyclerView.getChildAt(newPos);
        Button addButt = v.findViewById(R.id.add_button);
        view.setEnabled(false);
        addButt.setEnabled(true);
        AppContact contactToRemove = contactsList.get(pos);
        contactToRemove.setIsAdded(false);
        contactsToAdd.remove(contactToRemove);
    }

    public void onClickAddGroup(View view) {
        EditText groupNameView = findViewById(R.id.added_group_name_edit_text);
        String groupName = groupNameView.getText().toString();

        if (groupName.matches("")) {
            Toast.makeText(this, "No group name was entered ", Toast.LENGTH_LONG).show();
            return;
        }

        // Hiding the soft keyboard down
        View focused = this.getCurrentFocus();
        if (focused != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
        }

        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.done_button).setEnabled(true);

        view.setVisibility(View.INVISIBLE);

        groupPresenter.addGroup(groupName);

    }

    public void onClickDone(View view) {
        int size =  contactsToAdd.size();

        if (size == 0) {
            Toast.makeText(this, "No members were selected", Toast.LENGTH_LONG).show();
            return;
        }

        EditText groupNameView = findViewById(R.id.added_group_name_edit_text);
        String groupName = groupNameView.getText().toString();
        groupPresenter.refreshGroups();
        AppGroup selectedGroup = groupPresenter.getGroupByName(groupName);

        for (int i = 0; i < size; i++) {
            groupPresenter.addContactToGroup(contactsToAdd.get(i).getContact().getId(), selectedGroup.getGroup().getId());
        }

        unselectAll();

        finish();

        /*
        String mems = "";
        for (int i = 0; i < size; i++) {
            mems += contactsToAdd.get(i).getContact().getDisplayName();
            mems += " ";
        }
        Toast.makeText(getApplicationContext(),"Members: " + mems, Toast.LENGTH_LONG).show();
        */
    }

    private void fillNameList() {
        int size = contactsList.size();
        for (int i = 0; i < size; i++ ) {
            contactsNameList.add(contactsList.get(i).getContact().getDisplayName());
        }
    }

    private void unselectAll() {
        View v;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisiblePos = linearLayoutManager.findFirstVisibleItemPosition();
        int visibleItemCount = linearLayoutManager.findLastVisibleItemPosition() - firstVisiblePos + 1;
        int itemCount = linearLayoutManager.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            AppContact ac = contactsList.get(i);
            ac.setIsSelected(false);
            ac.setIsAdded(false);
        }
        for (int i = 0; i < visibleItemCount; i++) {
            v = recyclerView.getChildAt(i);
            Button addButt = v.findViewById(R.id.add_button);
            addButt.setEnabled(true);
            Button removeButt = v.findViewById(R.id.remove_button);
            removeButt.setEnabled(false);
        }
    }

    public void unSelectContact(int pos) {
        contactsList.get(pos).setIsSelected(false);
    }

}
