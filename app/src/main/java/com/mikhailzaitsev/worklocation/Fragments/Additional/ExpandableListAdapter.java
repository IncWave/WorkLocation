package com.mikhailzaitsev.worklocation.Fragments.Additional;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mikhailzaitsev.worklocation.Db.Group;
import com.mikhailzaitsev.worklocation.R;

import java.util.ArrayList;
import java.util.Objects;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Group> groupArrayList;

    LayoutInflater inflaterGroup;
    LayoutInflater inflaterChild;

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
        return getGroup(i).getGroupId();
    }


    @Override
    public long getChildId(int i, int i1) {
        return getChild(i, i1).getMemberId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
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
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public View getGroupView(int groupN, boolean b, View view, ViewGroup viewGroup) {
        if(view == null){
            inflaterGroup = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflaterGroup.inflate(R.layout.list_group,null);
        }
        TextView id = view.findViewById(R.id.id_group);
        id.setText(String.valueOf(getGroup(groupN).getGroupId()));
        TextView groupName = view.findViewById(R.id.list_group_name);
        groupName.setText(getGroup(groupN).getGroupName());
        TextView groupNumberOfMembers = view.findViewById(R.id.list_group_number_of_online);
        String numOfOnlineAndOffline = findOutHowManyMembersAreOnline(getGroup(groupN)) + " / " + getGroup(groupN).getMemberSize();
        groupNumberOfMembers.setText(numOfOnlineAndOffline);

        return view;
    }

    @Override
    public View getChildView(int groupN, int itemN, boolean b, View view, ViewGroup viewGroup) {
        if (view == null){
            inflaterChild = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflaterChild.inflate(R.layout.list_item,null);
        }

        TextView idGroup = view.findViewById(R.id.list_item_id_group);
        idGroup.setText(String.valueOf(getGroup(groupN).getGroupId()));

        TextView idChild = view.findViewById(R.id.list_item_id_item);
        idChild.setText(String.valueOf(getGroup(groupN).getMembers().get(itemN).getMemberId()));

        TextView itemNameTextView = view.findViewById(R.id.list_item_name);
        itemNameTextView.setText(getGroup(groupN).getMembers().get(itemN).getMemberName());

        ImageView isOnlineImageView = view.findViewById(R.id.list_item_online);
        if (getGroup(groupN).getMembers().get(itemN).isOnline()){
            isOnlineImageView.setImageResource(R.drawable.is_online18dp);
        }else {
            isOnlineImageView.setImageResource(R.drawable.is_not_online18dp);
        }
        return view;
    }

    public void theDeleteButtonChanged(){
        notifyDataSetChanged();
    }

}
