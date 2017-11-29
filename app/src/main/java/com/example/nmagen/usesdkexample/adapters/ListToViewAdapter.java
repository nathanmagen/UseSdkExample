package com.example.nmagen.usesdkexample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.MobileTornado.sdk.model.data.Group;
import com.example.nmagen.usesdkexample.R;

import java.util.List;

/**
 * Created by nmagen on 28/11/2017.
 */

public class ListToViewAdapter extends RecyclerView.Adapter {
    private List<Group> groupList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewHolder(TextView tv) {
            super(tv);
            textView = tv;
        }
    }

    public ListToViewAdapter(List<Group> gList) {
        groupList = gList;
    }

    @Override
    public ListToViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder lHolder = (ViewHolder)holder;
        lHolder.textView.setText(groupList.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
