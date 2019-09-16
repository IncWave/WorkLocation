package com.mikhailzaitsev.worklocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhailzaitsev.worklocation.Db.Group;

import java.util.ArrayList;
import java.util.Objects;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Group> groupArrayList;
    private boolean deleteButtonPushed = false;
    private ArrayList<Integer> listOfChoosedGroups;
    private ArrayList<ArrayList<Integer>> listOfChoosedItems;

    public ExpandableListAdapter(Context context, ArrayList<Group> groupArrayList) {
        this.context = context;
        this.groupArrayList = groupArrayList;
    }

    @Override
    public int getGroupCount() {
        return groupArrayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int num;
        try {
            num = Objects.requireNonNull(groupArrayList.get(i)).getMemberSize();
        }catch (NullPointerException ex){
            return 0;
        }
        return num;
    }

    @Override
    public Group getGroup(int i) {
        return groupArrayList.get(i);
    }

    @Override
    public Group.Member getChild(int i, int i1) {
        return groupArrayList.get(i).getMembers().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group,null);
        }
        TextView groupName = view.findViewById(R.id.list_group_name);
        groupName.setText(getGroup(i).getGroupName());

        TextView groupNumberOfMembers = view.findViewById(R.id.list_group_number_of_online);
        String numOfOnlineAndOffline = findOutHowManyMembersAreOnline(getGroup(i)) + " / " + getGroup(i).getMemberSize();
        groupNumberOfMembers.setText(numOfOnlineAndOffline);

        CheckBox groupCheckBox = view.findViewById(R.id.list_group_checkbox);
        if (deleteButtonPushed){
            groupCheckBox.setVisibility(View.VISIBLE);
        }else {
            groupCheckBox.setVisibility(View.GONE);
        }

        groupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return view;
    }

    private int findOutHowManyMembersAreOnline(Group group){
        int i = 0;
        int numOfOnline = 0;
        while (group.getMembers().size() != i){
            if (group.getMembers().get(i).isOnline()){
                numOfOnline++;
            }
            i++;
        }
        return numOfOnline;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item,null);
        }

        TextView itemNameTextView = view.findViewById(R.id.list_item_name);
        itemNameTextView.setText(getGroup(i).getMembers().get(i1).getMemberName());

        ImageView isOnlineImageView = view.findViewById(R.id.list_item_online);
        if (getGroup(i).getMembers().get(i1).isOnline()){
            isOnlineImageView.setImageResource(R.drawable.is_online18dp);
        }else {
            isOnlineImageView.setImageResource(R.drawable.is_not_online_18dp);
        }

        CheckBox itemCheckBox = view.findViewById(R.id.list_item_checkbox);
        if (deleteButtonPushed){
            itemCheckBox.setVisibility(View.VISIBLE);
        }else {
            itemCheckBox.setVisibility(View.GONE);
        }

        itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    public void theDeleteButtonChanged(){
        if (deleteButtonPushed){
            deleteButtonPushed = false;
        }else {
            deleteButtonPushed = true;
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
