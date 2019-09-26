package com.mikhailzaitsev.worklocation;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhailzaitsev.worklocation.Db.Group;

import java.util.ArrayList;
import java.util.Objects;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Group> groupArrayList;
    private boolean deleteButtonPushed = false;
    private static SparseArray <ArrayList<Integer>> listOfChoosedItems;

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

    @Override
    public View getGroupView(int groupN, boolean b, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group,null);
        }
        TextView groupName = view.findViewById(R.id.list_group_name);
        groupName.setText(getGroup(groupN).getGroupName());

        TextView groupNumberOfMembers = view.findViewById(R.id.list_group_number_of_online);
        String numOfOnlineAndOffline = findOutHowManyMembersAreOnline(getGroup(groupN)) + " / " + getGroup(groupN).getMemberSize();
        groupNumberOfMembers.setText(numOfOnlineAndOffline);

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
    public View getChildView(final int groupN, final int itemN, boolean b, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item,null);
        }

        TextView itemNameTextView = view.findViewById(R.id.list_item_name);
        itemNameTextView.setText(getGroup(groupN).getMembers().get(itemN).getMemberName());

        ImageView isOnlineImageView = view.findViewById(R.id.list_item_online);
        if (getGroup(groupN).getMembers().get(itemN).isOnline()){
            isOnlineImageView.setImageResource(R.drawable.is_online18dp);
        }else {
            isOnlineImageView.setImageResource(R.drawable.is_not_online18dp);
        }

        CheckBox itemCheckBox = view.findViewById(R.id.list_item_checkbox);
        if (listOfChoosedItems == null){
            itemCheckBox.setChecked(false);
        }
        itemCheckBox.setTag(new NewTag(groupN, itemN));
        if (deleteButtonPushed){
            itemCheckBox.setVisibility(View.VISIBLE);
        }else {
            itemCheckBox.setVisibility(View.GONE);
        }

        itemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean is) {
                NewTag newTag = (NewTag) view.getTag();
                if (is){
                    if (listOfChoosedItems == null){
                        listOfChoosedItems = new SparseArray<>();
                    }
                    if (listOfChoosedItems.get(newTag.groupN) == null){
                        ArrayList<Integer> firstItem = new ArrayList<>();
                        firstItem.add(newTag.itemN);
                        listOfChoosedItems.put(newTag.groupN, firstItem);
                    }else {
                        listOfChoosedItems.get(newTag.groupN).add(newTag.itemN);
                    }
                }else {
                    if (listOfChoosedItems != null){
                        listOfChoosedItems.get(newTag.groupN).remove((Integer) newTag.itemN);
                    }
                }
            }
        });
        return view;
    }

    public void theDeleteButtonChanged(){
        deleteButtonPushed = !deleteButtonPushed;
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public static SparseArray <ArrayList<Integer>> getListOfChoosedItems(){
        return listOfChoosedItems;
    }
    public static void setNullListOfChoosedItems(){
        listOfChoosedItems = null;
    }

    private class NewTag{
        private int groupN;
        private int itemN;

        private NewTag(int groupN, int itemN) {
            this.groupN = groupN;
            this.itemN = itemN;
        }
    }
}
