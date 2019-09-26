package com.mikhailzaitsev.worklocation.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.Db.Group;
import com.mikhailzaitsev.worklocation.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.mikhailzaitsev.worklocation.Fragments.GroupsFragment.*;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Group> groupArrayList;

    ExpandableListAdapter(Context context, ArrayList<Group> groupArrayList) {
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
    public View getGroupView(final int groupN, boolean b, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group,null);
        }
        TextView groupName = view.findViewById(R.id.list_group_name);
        groupName.setText(getGroup(groupN).getGroupName());

        TextView groupNumberOfMembers = view.findViewById(R.id.list_group_number_of_online);
        String numOfOnlineAndOffline = findOutHowManyMembersAreOnline(getGroup(groupN)) + " / " + getGroup(groupN).getMemberSize();
        groupNumberOfMembers.setText(numOfOnlineAndOffline);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!firstPressed[0]){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setMessage(view.getResources().getString(R.string.sure_delete_g));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, view.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Db.newInstance().deleteGroup(groupN);
                            notifyDataSetChanged();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, view.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                }
                if (!firstPressed[2]){

                }
                return false;
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstPressed[0]){
                   AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                   alertDialog.setMessage(view.getResources().getString(R.string.sure_delete_m));
                   alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, view.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           Db.newInstance().deleteMember(groupN,itemN);
                           notifyDataSetChanged();
                       }
                   });
                   alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, view.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                       }
                   });
                   alertDialog.show();
                }
                if (!firstPressed[2]){

                }
            }
        });
        return view;
    }

    void theDeleteButtonChanged(){
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /*private class FireDialogFragment extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if (!firstPressed[0]) {
                builder.setMessage(R.string.sure_delete_q).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete = true;
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete = false;
                    }
                });

            }
            return builder.create();
        }
    }*/
}
