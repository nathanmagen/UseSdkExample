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

import com.MobileTornado.sdk.model.data.Contact;
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
    PresentersManager presentersManager = PresentersManager.getInstance();
    GroupPresenter groupPresenter = presentersManager.getGroupPresenter();
    List<AppContact> contactsList = groupPresenter.getContacts();
    List<String> contactsNameList = new ArrayList<>();
    List<AppContact> contactsToAdd = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        // Setting focus on the group name box and requesting to pop up the keyboard
        /*
        EditText groupNameView = findViewById(R.id.added_group_name_edit_text);
        InputMethodManager inpMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inpMan.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        groupNameView.setFocusableInTouchMode(true);
        */
        // Setting the recyclerView
        recyclerView = findViewById(R.id.contacts_list_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        fillNameList();
        adapter = new ListToViewAdapter(contactsNameList, R.layout.contact_list_line, R.id.contact_name_view, R.id.add_button);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
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
                int listSize = contactsList.size();
                for (int i = 0; i < listSize; i++) {
                    if (position != i) {
                        contactsList.get(i).setIsSelected(false);
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
        View v = recyclerView.getChildAt(pos);
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
        View v = recyclerView.getChildAt(pos);
        Button addButt = v.findViewById(R.id.add_button);
        view.setEnabled(false);
        addButt.setEnabled(true);
        AppContact contactToRemove = contactsList.get(pos);
        contactToRemove.setIsAdded(false);
        contactsToAdd.remove(contactToRemove);
    }

    public void onClickDone(View view) {
        int size =  contactsToAdd.size();
        String mems = "";
        for (int i = 0; i < size; i++) {
            mems += contactsToAdd.get(i).getContact().getDisplayName();
            mems += " ";
        }
        Toast.makeText(getApplicationContext(),"Members: " + mems, Toast.LENGTH_LONG).show();
    }

    private void fillNameList() {
        int size = contactsList.size();
        for (int i = 0; i < size; i++ ) {
            contactsNameList.add(contactsList.get(i).getContact().getDisplayName());
        }
    }
}
