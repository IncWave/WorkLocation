package com.mikhailzaitsev.worklocation.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.ExpandableListAdapter;
import com.mikhailzaitsev.worklocation.R;


public class GroupsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    //private String mParam1;

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private boolean firstPressed = true;


    public GroupsFragment() {
    }

    public static GroupsFragment newInstance(String param1) {
        GroupsFragment fragment = new GroupsFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
          //  mParam1 = getArguments().getString(ARG_PARAM1);
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        listView = view.findViewById(R.id.fragment_group_expendable_listview);
        listAdapter = new ExpandableListAdapter(getContext(), Db.newInstance().getGroupArray());
        listView.setAdapter(listAdapter);


        ImageButton addButton = view.findViewById(R.id.fragment_group_add_button);
        ImageButton deleteButton = view.findViewById(R.id.fragment_group_delete_button);
        setOnClickListener(addButton, listAdapter);
        setOnClickListener(deleteButton, listAdapter);
        return view;
    }

    private void setOnClickListener(ImageButton button, final ExpandableListAdapter listAdapter){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.fragment_group_add_button:

                        break;
                    case R.id.fragment_group_delete_button:
                        if (firstPressed){
                            listAdapter.theDeleteButtonChanged();
                            firstPressed = !firstPressed;
                        }else {
                            if (ExpandableListAdapter.getListOfChoosedItems() != null) {
                                Db.newInstance().deleteMember(ExpandableListAdapter.getListOfChoosedItems());
                                ExpandableListAdapter.setNullListOfChoosedItems();
                            }
                            firstPressed = !firstPressed;
                            listAdapter.theDeleteButtonChanged();
                        }
                        break;
                }
            }
        });
    }

}
