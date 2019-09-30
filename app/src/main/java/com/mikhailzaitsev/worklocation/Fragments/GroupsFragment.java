package com.mikhailzaitsev.worklocation.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.R;


public class GroupsFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private ImageButton deleteButton;
    private ImageButton addButton;
    private ImageButton editButton;
    private static boolean[] firstPressed = new boolean[]{true,true,true};

    public GroupsFragment() {
    }

    public static GroupsFragment newInstance() {
        return new GroupsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        deleteButton = view.findViewById(R.id.fragment_group_delete_button);
        addButton = view.findViewById(R.id.fragment_group_add_button);
        editButton = view.findViewById(R.id.fragment_group_edit_button);

        listView = view.findViewById(R.id.fragment_group_expendable_listview);
        listAdapter = new ExpandableListAdapter(getContext(), Db.newInstance().getGroupArray());
        listView.setAdapter(listAdapter);

        setOnClickListener(deleteButton, listAdapter);
        setOnClickListener(addButton, listAdapter);
        setOnClickListener(editButton, listAdapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, final int groupN, final int itemN, long l) {
                if (!firstPressed[0]){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setMessage(view.getResources().getString(R.string.sure_delete_m));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, view.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Db.newInstance().deleteMember(groupN,itemN);
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, view.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                }
                return false;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                if (!firstPressed[0]){
                    AlertDialog alertDialogDelete = new AlertDialog.Builder(view.getContext()).create();
                    alertDialogDelete.setMessage(view.getResources().getString(R.string.sure_delete_g));
                    alertDialogDelete.setButton(AlertDialog.BUTTON_POSITIVE, view.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TextView id = view.findViewById(R.id.id_group);
                            Db.newInstance().deleteGroupById(id.getText().toString());
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                    alertDialogDelete.setButton(AlertDialog.BUTTON_NEGATIVE, view.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialogDelete.show();
                }
                if (!firstPressed[2]){
                    // create an alert alertDialogEdit
                    AlertDialog.Builder alertDialogEdit = new AlertDialog.Builder(getContext());
                    alertDialogEdit.setTitle(getResources().getString(R.string.name));

                    // set the custom layout
                    final View customDialog = getLayoutInflater().inflate(R.layout.edit_dialog,null);
                    alertDialogEdit.setView(customDialog);
                    final EditText editText = customDialog.findViewById(R.id.edit_dialog_text);

                    final TextView idTextView = view.findViewById(R.id.id_group);
                    final String idText = String.valueOf(idTextView.getText());
                    String nameBefore = Db.newInstance().getGroupNameById(idText);
                    editText.setText(nameBefore);

                    //add a button
                    alertDialogEdit.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Db.newInstance().changeGroupById(idText,editText.getText().toString());
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                    alertDialogEdit.show();
                }
                return false;
            }
        });
        return view;
    }

    private void setOnClickListener(ImageButton button, final ExpandableListAdapter listAdapter){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.fragment_group_delete_button:
                        if (firstPressed[0]){
                            addButton.setEnabled(false);
                            editButton.setEnabled(false);
                            deleteButton.setImageResource(R.drawable.delete_pink_48dp);
                            firstPressed[0] = !firstPressed[0];
                        }else {
                            deleteButton.setImageResource(R.drawable.delete_grey_48dp);
                            firstPressed[0] = !firstPressed[0];
                            addButton.setEnabled(true);
                            editButton.setEnabled(true);
                        }
                        break;
                    case R.id.fragment_group_add_button:
                        if (firstPressed[1]){
                            deleteButton.setEnabled(false);
                            editButton.setEnabled(false);
                            addButton.setImageResource(R.drawable.add_pink_48dp);
                            firstPressed[1] = !firstPressed[1];
                        }else {
                            addButton.setImageResource(R.drawable.add_grey_48dp);
                            firstPressed[1] = !firstPressed[1];
                            deleteButton.setEnabled(true);
                            editButton.setEnabled(true);
                        }
                        break;
                    case R.id.fragment_group_edit_button:
                        if (firstPressed[2]){
                            deleteButton.setEnabled(false);
                            addButton.setEnabled(false);
                            editButton.setImageResource(R.drawable.edit_pink_48dp);
                            firstPressed[2] = !firstPressed[2];
                        }else {
                            editButton.setImageResource(R.drawable.edit_grey_48dp);
                            firstPressed[2] = !firstPressed[2];
                            deleteButton.setEnabled(true);
                            addButton.setEnabled(true);
                            MapFragment.newInstance().initialiseMap();
                        }
                        break;
                }
                listAdapter.theDeleteButtonChanged();
            }
        });
    }

}
