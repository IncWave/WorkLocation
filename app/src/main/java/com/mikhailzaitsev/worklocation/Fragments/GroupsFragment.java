package com.mikhailzaitsev.worklocation.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.R;


public class GroupsFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private ImageButton deleteButton;
    private ImageButton addButton;
    private ImageButton editButton;
    static boolean[] firstPressed = new boolean[]{true,true,true};

    public GroupsFragment() {
    }

    public static GroupsFragment newInstance() {
        GroupsFragment fragment = new GroupsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        listView = view.findViewById(R.id.fragment_group_expendable_listview);
        listAdapter = new ExpandableListAdapter(getContext(), Db.newInstance().getGroupArray());
        listView.setAdapter(listAdapter);


        deleteButton = view.findViewById(R.id.fragment_group_delete_button);
        addButton = view.findViewById(R.id.fragment_group_add_button);
        editButton = view.findViewById(R.id.fragment_group_edit_button);
        setOnClickListener(deleteButton, listAdapter);
        setOnClickListener(addButton, listAdapter);
        setOnClickListener(editButton, listAdapter);
        return view;
    }

    private void setOnClickListener(ImageButton button, final ExpandableListAdapter listAdapter){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.fragment_group_delete_button:
                        if (firstPressed[0]){
                            deleteButton.setImageResource(R.drawable.delete_pink_48dp);
                            firstPressed[0] = !firstPressed[0];
                            addButton.setEnabled(false);
                            editButton.setEnabled(false);
                        }else {
                            deleteButton.setImageResource(R.drawable.delete_grey_48dp);
                            firstPressed[0] = !firstPressed[0];
                            addButton.setEnabled(true);
                            editButton.setEnabled(true);
                        }
                        break;
                    case R.id.fragment_group_add_button:
                        if (firstPressed[1]){
                            addButton.setImageResource(R.drawable.add_pink_48dp);
                            firstPressed[1] = !firstPressed[1];
                            deleteButton.setEnabled(false);
                            editButton.setEnabled(false);
                        }else {
                            addButton.setImageResource(R.drawable.add_grey_48dp);
                            firstPressed[1] = !firstPressed[1];
                            deleteButton.setEnabled(true);
                            editButton.setEnabled(true);
                        }
                        break;
                    case R.id.fragment_group_edit_button:
                        if (firstPressed[2]){
                            editButton.setImageResource(R.drawable.edit_pink_48dp);
                            firstPressed[2] = !firstPressed[2];
                            deleteButton.setEnabled(false);
                            addButton.setEnabled(false);
                        }else {
                            editButton.setImageResource(R.drawable.edit_grey_48dp);
                            firstPressed[2] = !firstPressed[2];
                            deleteButton.setEnabled(true);
                            addButton.setEnabled(true);
                        }
                        break;
                }
                listAdapter.theDeleteButtonChanged();
            }
        });
    }

}
