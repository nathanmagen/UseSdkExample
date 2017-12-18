package com.example.nmagen.usesdkexample.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.adapters.ListToViewAdapter;
import com.example.nmagen.usesdkexample.data.AppGroup;
import com.example.nmagen.usesdkexample.presenters.GroupPresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;

import java.util.List;

public class ShowMembersActivity extends AppCompatActivity {
    PresentersManager presentersManager = PresentersManager.getInstance();
    GroupPresenter groupPresenter = presentersManager.getGroupPresenter();
    List<AppGroup> groupList = groupPresenter.getGroupList();
    List<String> groupMembersNameList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_members);

        // Getting the selected group to show the members, and it's name list
        Intent intent = getIntent();
        int selectedGroupPos = intent.getIntExtra(GroupListActivity.SELECTED_GROUP_KEY, 0);
        AppGroup selectedGroup = groupList.get(selectedGroupPos);
        groupMembersNameList = groupPresenter.getGroupMembersNameList(selectedGroup);

        // Showing the group name in the text view
        ((TextView) findViewById(R.id.groupName2ShowMembers)).setText(selectedGroup.getGroup().getDisplayName());

        // Setting up the recyclerView
        recyclerView = findViewById(R.id.group_member_list_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ListToViewAdapter(this, groupMembersNameList, R.layout.group_member_list_line, R.id.groupMemberName, ListToViewAdapter.NO_BUTTON);
        recyclerView.setAdapter(adapter);
    }


}
