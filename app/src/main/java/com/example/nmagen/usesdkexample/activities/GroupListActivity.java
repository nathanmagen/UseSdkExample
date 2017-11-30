package com.example.nmagen.usesdkexample.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nmagen.usesdkexample.adapters.ListToViewAdapter;
import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.data.AppGroup;
import com.example.nmagen.usesdkexample.presenters.GroupPresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;

import java.util.List;

import listeners.RecyclerTouchListener;

public class GroupListActivity extends AppCompatActivity {
    private PresentersManager presentersManager = PresentersManager.getInstance();
    private GroupPresenter groupPresenter = presentersManager.getGroupPresenter();
    private List<AppGroup> groupList = groupPresenter.getGroupList();
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
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ListToViewAdapter(groupList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                AppGroup clickedGroup = groupList.get(position);
                if (!clickedGroup.isSelected()) {
                    // select procedure, consider moving to private method
                    clickedGroup.setSelected();
                    view.findViewById(R.id.pttButton).setEnabled(true);
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                else {
                    // un select procedure, consider moving to private method
                    clickedGroup.setUnSelected();
                    view.findViewById(R.id.pttButton).setEnabled(false);
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    view.setClickable(true);
                }
                int listSize = groupList.size();
                for (int i = 0; i < listSize; i++) {
                    if (position != i) {
                        groupList.get(i).setUnSelected();
                        View v = recyclerView.getChildAt(i);
                        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        v.findViewById(R.id.pttButton).setEnabled(false);
                        v.setClickable(true);
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void onPttClick(View view) {
        int pos = (int) view.getTag();
        AppGroup ag = groupList.get(pos);
        Toast.makeText(this, "PTT of " + ag.getGroup().getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}
