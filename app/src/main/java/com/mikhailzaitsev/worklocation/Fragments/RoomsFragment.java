package com.mikhailzaitsev.worklocation.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.mikhailzaitsev.worklocation.Db.Room;
import com.mikhailzaitsev.worklocation.ExpandableListAdapter;
import com.mikhailzaitsev.worklocation.R;


public class RoomsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;


    public RoomsFragment() {
    }

    public static RoomsFragment newInstance(String param1) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);
        listView = (ExpandableListView) view.findViewById(R.id.fragment_room_expendable_listview);
        listAdapter = new ExpandableListAdapter(getContext(), Room.makeRoom());
        listView.setAdapter(listAdapter);
        return view;
    }

}
