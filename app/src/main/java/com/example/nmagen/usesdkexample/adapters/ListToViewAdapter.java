package com.example.nmagen.usesdkexample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.data.AppGroup;

import java.util.List;

/**
 * Created by nmagen on 28/11/2017.
 */

public class ListToViewAdapter extends RecyclerView.Adapter {
    private List<AppGroup> groupList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupNameView;
        public Button pttButton;
        ViewHolder(View v) {
            super(v);
            groupNameView = v.findViewById(R.id.groupName);
            pttButton = v.findViewById(R.id.pttButton);
        }
    }

    public ListToViewAdapter(List<AppGroup> gList) {
        groupList = gList;
    }

    @Override
    public ListToViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_line, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder lHolder = (ViewHolder)holder;
        lHolder.groupNameView.setText(groupList.get(position).getGroup().getDisplayName());
        lHolder.pttButton.setTag(position);
        lHolder.pttButton.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
