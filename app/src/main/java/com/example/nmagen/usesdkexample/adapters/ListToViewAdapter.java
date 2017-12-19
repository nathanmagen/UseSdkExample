package com.example.nmagen.usesdkexample.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nmagen.usesdkexample.R;
import com.example.nmagen.usesdkexample.activities.AddGroupActivity;
import com.example.nmagen.usesdkexample.activities.FourButtonsActivity;
import com.example.nmagen.usesdkexample.activities.GroupListActivity;
import com.example.nmagen.usesdkexample.activities.ShowMembersActivity;

import java.util.List;

/**
 * Created by nmagen on 28/11/2017.
 */

public class ListToViewAdapter extends RecyclerView.Adapter {
    public static final int NO_BUTTON = -1;
    private List<String> groupList;
    private Activity activity;
    private int layoutId, textViewId, buttonId;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView groupNameView;
        public Button selectButton;
        public View view;
        ViewHolder(View v, int textViewId, int buttonId) {
            super(v);
            view = v;
            groupNameView = v.findViewById(textViewId);
            if ( buttonId != NO_BUTTON) {
                selectButton = v.findViewById(buttonId);
            }
            else {
                selectButton = null;
            }
        }
    }

    public ListToViewAdapter(Activity a, List<String> gList, int lId, int tvId, int bId) {
        activity = a;
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
        lHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        if (lHolder.selectButton != null) {
            lHolder.selectButton.setEnabled(false);
            lHolder.selectButton.setTag(position); // Tagging the button with the position so it would be available in the activity
        }

        if (activity instanceof GroupListActivity) {
            GroupListActivity a = (GroupListActivity)activity;
            a.unSelectGroup(position);
        }
        else if (activity instanceof AddGroupActivity) {
            AddGroupActivity a = (AddGroupActivity)activity;
            a.unSelectContact(position);
        }
        else if (activity instanceof FourButtonsActivity) {
            FourButtonsActivity a = (FourButtonsActivity) activity;
            if (!a.isNoCallOptions()) {
                a.unSelectCallOption(position);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        ViewHolder lHolder = (ViewHolder) holder;
        int pos = lHolder.getAdapterPosition();
        if (activity instanceof GroupListActivity) {
            GroupListActivity glActivity = (GroupListActivity) activity;
            if (pos != glActivity.getClickedOnGroupPosition()) {
                glActivity.unSelectGroup(pos);
                lHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                lHolder.selectButton.setEnabled(false);
            }
            else {
                lHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                lHolder.selectButton.setEnabled(true);
            }
        }
        else if (activity instanceof AddGroupActivity) {
            AddGroupActivity agActivity = (AddGroupActivity) activity;
            if (pos != agActivity.getClickedOnContactPosition()) {
                agActivity.unSelectContact(pos);
                lHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                lHolder.view.findViewById(R.id.add_button).setEnabled(false);
                lHolder.view.findViewById(R.id.remove_button).setEnabled(false);
            }
            else {
                lHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                if (agActivity.isContactAdded(pos)) {
                    lHolder.view.findViewById(R.id.add_button).setEnabled(false);
                    lHolder.view.findViewById(R.id.remove_button).setEnabled(true);
                }
                else {
                    lHolder.view.findViewById(R.id.add_button).setEnabled(true);
                    lHolder.view.findViewById(R.id.remove_button).setEnabled(false);
                }
            }
        }
        else if (activity instanceof FourButtonsActivity) {
            FourButtonsActivity fbActivity = (FourButtonsActivity) activity;
            if (!fbActivity.isNoCallOptions()) {
                if (pos != fbActivity.getClickedCallOptionPosition()) {
                    fbActivity.unSelectCallOption(pos);
                    lHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                }
                else {
                    lHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
                }

                if (pos != fbActivity.getLongClickedOnPosition()) {
                    lHolder.view.findViewById(R.id.image_button_delete).setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
