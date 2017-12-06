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
    private List<String> groupList;
    private int layoutId;
    private int textViewId;
    private int buttonId;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupNameView;
        public Button selectButton;
        ViewHolder(View v, int textViewId, int buttonId) {
            super(v);
            groupNameView = v.findViewById(textViewId);
            selectButton = v.findViewById(buttonId);
        }
    }

    public ListToViewAdapter(List<String> gList, int lId, int tvId, int bId) {
        groupList = gList;
        layoutId = lId;
        textViewId = tvId;
        buttonId = bId;
    }

    @Override
    public ListToViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(v, textViewId, buttonId);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder lHolder = (ViewHolder)holder;
        lHolder.groupNameView.setText(groupList.get(position));
        lHolder.selectButton.setTag(position); // Tagging the button with the position so it would be available in the activity
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
