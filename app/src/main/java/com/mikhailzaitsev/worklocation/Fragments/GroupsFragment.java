package com.mikhailzaitsev.worklocation.Fragments;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.Fragments.Additional.ExpandableListAdapter;
import com.mikhailzaitsev.worklocation.R;

import java.util.Objects;


public class GroupsFragment extends Fragment {

    private ExpandableListAdapter listAdapter;
    private ImageButton deleteButton;
    private ImageButton addButton;
    private ImageButton editButton;
    private static boolean[] firstPressed = new boolean[]{true,true,true};
    private FusedLocationProviderClient fusedLocationProvider;
    private String currentUserIdThatCouldBeShowed = "";

    private static final String ARG = "param1";


    public GroupsFragment() {
    }

    public static GroupsFragment newInstance(String string) {
        GroupsFragment fragment = new GroupsFragment();
       fragment.currentUserIdThatCouldBeShowed = string;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        deleteButton = view.findViewById(R.id.fragment_group_delete_button);
        addButton = view.findViewById(R.id.fragment_group_add_button);
        editButton = view.findViewById(R.id.fragment_group_edit_button);

        ExpandableListView listView = view.findViewById(R.id.fragment_group_expendable_listview);
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
                            createChooseDialog();
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
                        }
                        break;
                }
                listAdapter.theDeleteButtonChanged();
            }
        });
    }

    private void createChooseDialog(){
        String [] choose = { "Get My Id", "Create Group...", "Add Group...", "Add Member..."};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setItems(choose, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                dialogInterface.dismiss();
                                addButton.callOnClick();
                                getMyIdDialog();
                                break;
                            case 1:
                                dialogInterface.dismiss();
                                addButton.callOnClick();
                                createNewGroupDialog();
                                break;
                            case 2:
                                dialogInterface.dismiss();
                                addButton.callOnClick();
                                addGroupDialog();
                                break;
                            case 3:
                                dialogInterface.dismiss();
                                addButton.callOnClick();
                                addMemberDialog();
                                break;
                        }
                    }
                });
        builder.show();

    }

    private void getMyIdDialog(){
        final AlertDialog builder = new AlertDialog.Builder(getContext()).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.get_my_id_dialog,null);
        final EditText editText = dialogView.findViewById(R.id.get_my_id_dialog_edit_text);
        editText.setText(currentUserIdThatCouldBeShowed);
        Button okButton = dialogView.findViewById(R.id.get_my_id_dialog_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", editText.getText().toString());
                clipboard.setPrimaryClip(clip);
                builder.dismiss();
                Toast.makeText(getContext(),"Your id was copied",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(dialogView);
        builder.show();
    }

    private void createNewGroupDialog(){
        if (Db.newInstance().getGroupArray().size()>=100){
            Toast.makeText(getContext(),"There's a limit to 100 groups",Toast.LENGTH_LONG).show();
        }else {
            fusedLocationProvider.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                        Db.newInstance().setLocation(location);
                        final AlertDialog builder1 = new AlertDialog.Builder(getContext()).create();
                        final View dialogView = getLayoutInflater().inflate(R.layout.create_group_dialog,null);
                        final EditText editText = dialogView.findViewById(R.id.create_group_dialog_set_name);
                        Button okButton = dialogView.findViewById(R.id.create_group_dialog_ok);
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (editText.getText().length() != 0){
                                    Db.newInstance().createGroup(editText.getText().toString(),50);
                                    builder1.dismiss();
                                }
                            }
                        });
                        builder1.setView(dialogView);
                        builder1.show();
                    }else {
                        Toast.makeText(getContext(),"Your last known location wasn't defined",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void addGroupDialog(){
        final AlertDialog builder1 = new AlertDialog.Builder(getContext()).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.add_group_dialog,null);
        final EditText editText = dialogView.findViewById(R.id.add_group_dialog_set_name);
        Button okButton = dialogView.findViewById(R.id.add_group_dialog_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////////////////////////////////////////////////
            }
        });
        builder1.setView(dialogView);
        builder1.show();
    }

    private void addMemberDialog(){
        final AlertDialog builder1 = new AlertDialog.Builder(getContext()).create();
        final View dialogView = getLayoutInflater().inflate(R.layout.add_member,null);
        final Spinner spinner = dialogView.findViewById(R.id.add_member_select_group_view);
        final EditText editText = dialogView.findViewById(R.id.add_member_id);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, Db.newInstance().getGroupNamesArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button okButton = dialogView.findViewById(R.id.add_member_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////////////////////////////////////////////////
            }
        });
        builder1.setView(dialogView);
        builder1.show();
    }

    public void dataHasBeenChanged(){
        listAdapter.notifyDataSetChanged();
    }
}
