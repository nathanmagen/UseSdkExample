package com.example.nmagen.usesdkexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.MobileTornado.sdk.model.data.Group;

import java.util.List;

public class GroupListActivity extends AppCompatActivity {
    private GroupPresenter groupPresenter = new GroupPresenter();
    private List<Group> groupList = groupPresenter.getGroupList();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        recyclerView = findViewById(R.id.group_list_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListToViewAdapter(groupList);
        recyclerView.setAdapter(adapter);
    }
}
