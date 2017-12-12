package com.example.nmagen.usesdkexample.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.MobileTornado.sdk.model.ContactsModule;
import com.MobileTornado.sdk.model.data.Group;
import com.MobileTornado.sdk.model.data.UserState;
import com.example.nmagen.usesdkexample.adapters.ListToViewAdapter;
import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.data.AppGroup;
import com.example.nmagen.usesdkexample.presenters.GroupPresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;

import java.util.ArrayList;
import java.util.List;

import com.example.nmagen.usesdkexample.listeners.RecyclerTouchListener;


public class GroupListActivity extends AppCompatActivity {
    private final int NO_GROUP_SELECTED = -1;
    public static final String SELECTED_GROUP_KEY = "com.example.nmagen.usesdkexample.activities";
    private PresentersManager presentersManager = PresentersManager.getInstance();
    private GroupPresenter groupPresenter = presentersManager.getGroupPresenter();
    private List<AppGroup> groupList;
    private List<String> groupNameList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int clickedOnGroupPosition = NO_GROUP_SELECTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        // Setting the user to not get calls
        presentersManager.getClientPresenter().setState(UserState.DND);

        groupPresenter.addGroupRemoveListener(new ContactsModule.GroupRemoveListener() {
            @Override
            public void onGroupRemoved(Group group) {
                Toast.makeText(getApplicationContext(), "The group was removed", Toast.LENGTH_SHORT).show();
            }
        });

        groupPresenter.refreshGroups();
        groupList = groupPresenter.getGroupList();
        fillNameList();

        // Setting up the recycler view
        recyclerView = findViewById(R.id.group_list_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ListToViewAdapter(this, groupNameList, R.layout.group_list_line, R.id.groupName, R.id.select_button);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickedOnGroupPosition = position;
                AppGroup clickedGroup = groupList.get(position);
                if (!clickedGroup.isSelected()) {
                    // select procedure, consider moving to private method
                    clickedGroup.setSelected();
                    view.findViewById(R.id.select_button).setEnabled(true);
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

                // unselecting all the rest of the groups in the list
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisiblePos = linearLayoutManager.findFirstVisibleItemPosition();
                int itemCount = linearLayoutManager.findLastVisibleItemPosition() - firstVisiblePos + 1;
                int newPos = position - firstVisiblePos;
                for (int i = 0; i < itemCount; i++) {
                    if (newPos != i) {
                        groupList.get(i + firstVisiblePos).setUnSelected();
                        View v = recyclerView.getChildAt(i);
                        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        v.findViewById(R.id.select_button).setEnabled(false);
                    }
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        groupPresenter.refreshGroups();
        groupList = groupPresenter.getGroupList();
        fillNameList();
        adapter.notifyDataSetChanged();
    }

    @Override // inflating the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.group_edit_menu, menu);
        return true;
    }

    @Override // triggered one of the icons is pressed
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.remove_group_item:
                removeGroup();
                break;
            case R.id.show_group_members_item:
                showGroupMembers();
                break;
            case R.id.add_group_item:
                addGroup();
                break;
        }

        return true;
    }

    public void onSelectClick(View view) {
        int pos = (int) view.getTag();
        Intent resultData = new Intent();
        resultData.putExtra(FourButtonsActivity.GROUP_TAG, pos);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    private void fillNameList() {
        int size = groupList.size();
        groupNameList.clear();
        for (int i = 0; i < size; i++ ) {
            groupNameList.add(groupList.get(i).getGroup().getDisplayName());
        }
    }

    public void unSelectGroup(int pos) {
        groupList.get(pos).setUnSelected();
    }

    public void removeGroup() {
        if (clickedOnGroupPosition == NO_GROUP_SELECTED) {
            Toast.makeText(this, "No group was selected", Toast.LENGTH_SHORT).show();
        }
        else {
            AppGroup group2Remove = groupList.get(clickedOnGroupPosition);
            if (group2Remove.getGroup().getType() != Group.Type.PERSONAL) {
                Toast.makeText(this, "Can not remove non personal group", Toast.LENGTH_SHORT).show();
            }
            else {
                groupPresenter.removeGroup(group2Remove.getGroup().getId());
                groupList.remove(clickedOnGroupPosition);
                clickedOnGroupPosition = NO_GROUP_SELECTED;

                Intent resultRemoveData = new Intent();
                resultRemoveData.putExtra(FourButtonsActivity.REMOVE_GROUP_TAG, group2Remove.getGroup().getDisplayName());
                setResult(FourButtonsActivity.RESULT_REMOVE_GROUP, resultRemoveData);
                finish();
            }
        }
    }

    public void showGroupMembers() {
        if (clickedOnGroupPosition == NO_GROUP_SELECTED) {
            Toast.makeText(this, "No group was selected", Toast.LENGTH_SHORT).show();
        }
        else {
            if (groupPresenter.isGroupEmpty(groupList.get(clickedOnGroupPosition))) {
                Toast.makeText(this, "No members in group", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(this, ShowMembersActivity.class);
                intent.putExtra(SELECTED_GROUP_KEY, clickedOnGroupPosition);
                startActivity(intent);
            }
        }
    }

    public void addGroup() {
        Intent intent = new Intent(this, AddGroupActivity.class);
        startActivity(intent);
    }

}
