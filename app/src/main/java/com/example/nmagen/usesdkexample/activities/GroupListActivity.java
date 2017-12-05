package com.example.nmagen.usesdkexample.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.MobileTornado.sdk.model.CallCallbacks;
import com.MobileTornado.sdk.model.data.CallInfo;
import com.MobileTornado.sdk.model.data.Contact;
import com.MobileTornado.sdk.model.data.UserState;
import com.example.nmagen.usesdkexample.adapters.ListToViewAdapter;
import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.data.AppGroup;
import com.example.nmagen.usesdkexample.presenters.CallPresenter;
import com.example.nmagen.usesdkexample.presenters.GroupPresenter;
import com.example.nmagen.usesdkexample.presenters.PresentersManager;
import com.example.nmagen.usesdkexample.presenters.SOSPresenter;

import java.io.Serializable;
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

        // Setting the user to not get calls
        presentersManager.getClientPresenter().setState(UserState.DND);

        // Setting up the recycler view
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
                    view.findViewById(R.id.select_button).setEnabled(true);
                    view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

                // unselecting all the rest of the groups in the list
                int listSize = groupList.size();
                for (int i = 0; i < listSize; i++) {
                    if (position != i) {
                        groupList.get(i).setUnSelected();
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

    public void onSelectClick(View view) {
        int pos = (int) view.getTag();
        Intent resultData = new Intent();
        resultData.putExtra(FourButtonsActivity.GROUP_TAG, pos);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }


}
