package com.mikhailzaitsev.worklocation.Db;

import android.location.Location;
import android.net.Uri;

import com.google.android.gms.maps.model.Circle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhailzaitsev.worklocation.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;


public class Db {
    private static ArrayList<Group> groupArrayList;
    private static Db db;
    private Location location;
    private boolean dataHasBeenChanged = false;
    private boolean onlineHasBeenChanged = false;

    private DatabaseReference dataR;

    ///////////////////////////////////получить индификатор моего аккаунта
    private String currentUserFirebaseId = "RdKxPTuHXRdNENmEd6vrg15dzTs1";
    ////////////////////////////////////

    private Db() {
        if (groupArrayList == null){

            //Connect to FirebaseDb
            dataR = FirebaseDatabase.getInstance().getReference();

            groupArrayList = new ArrayList<>();
            downloadListOfGroupsNames();
        }
    }

    public ArrayList<Group> getGroupArray(){
        return groupArrayList;
    }

    public ArrayList<String> getGroupNamesArray(){
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0 ; i < groupArrayList.size(); i++){
            arrayList.add(i,groupArrayList.get(i).getGroupName());
        }
        return arrayList;
    }

    public static Db newInstance() {
        if (db == null){
            db = new Db();
        }
        return db;
    }

    public ArrayList<Group> saveCirclesChanges(ArrayList<Circle> circleArrayList){
        Circle circle;
        for (int i=0; i<circleArrayList.size(); i++ ){
            circle = circleArrayList.get(i);
            groupArrayList.get(i).setLongitude(circle.getCenter().longitude);
            groupArrayList.get(i).setLatitude(circle.getCenter().latitude);
            groupArrayList.get(i).setRadius((int)circle.getRadius());
        }
        dataHasBeenChanged = true;
        return groupArrayList;
    }

    /*public ArrayList<Group> saveCircleChanges(Circle circle, int i){
        groupArrayList.get(i).setLongitude(circle.getCenter().longitude);
        groupArrayList.get(i).setLatitude(circle.getCenter().latitude);
        groupArrayList.get(i).setRadius((int)circle.getRadius());
        return groupArrayList;
    }*/



    public void deleteDb(){
        groupArrayList = null;
        db = null;
    }

    public String getGroupNameById(String id){
        Long groupId = Long.valueOf(id);
        for (int i = 0; i<groupArrayList.size(); i++){
            if (groupId.equals(groupArrayList.get(i).getGroupId())){
                return groupArrayList.get(i).getGroupName();
            }
        }
        return "";
    }

    public void deleteMember(int group, int member){
        groupArrayList.get(group).getMembers().remove(member);
        dataHasBeenChanged = true;
    }

    public void deleteGroupById(String group){
        Long id = Long.valueOf(group);
        for (Group group1 : groupArrayList){
            if (id.equals(group1.getGroupId())){
                groupArrayList.remove(group1);
                break;
            }
        }
        dataHasBeenChanged = true;
    }

    public void changeGroupById(String groupId, String name){
        Long id = Long.valueOf(groupId);
        for (int i = 0; i<groupArrayList.size(); i++){
            if (id.equals(groupArrayList.get(i).getGroupId())){
                groupArrayList.get(i).setGroupName(name);
                break;
            }
        }
        dataHasBeenChanged = true;
    }

    public void changeMyOnlineByUserId(ArrayList<String> arrayIdList, Boolean inOrNot){
        x:for (int j = 0; j < arrayIdList.size(); j++) {
            for (int i = 0; i < groupArrayList.size(); i++) {
                if (Long.parseLong(arrayIdList.get(j)) == (groupArrayList.get(i).getGroupId())) {
                    for (int k = 0; k < groupArrayList.get(i).getMembers().size(); k++){
                        if (currentUserFirebaseId.equals(groupArrayList.get(i).getMembers().get(k).getMemberIdFirebase())){
                            groupArrayList.get(i).getMembers().get(k).setOnline(inOrNot);
                            continue x;
                        }
                    }
                }
            }
        }
        onlineHasBeenChanged = true;
    }

    public void createGroup(String groupName, int radius){
        groupArrayList.add(new Group(
                Math.round( + radius + Calendar.getInstance().getTimeInMillis())/41
                ,groupName
                ,createMember()
                ,location.getLatitude()
                ,location.getLongitude()
                ,radius));
        dataHasBeenChanged = true;
        /////////////////////////////////////////////////////////////////////////send to the server
    }

    private ArrayList<Group.Member> createMember(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member(Calendar.getInstance().getTimeInMillis()
                ,MainActivity.getCurrentUserName()
                ,MainActivity.getCurrentUserUri(),
                true
                ,MainActivity.getCurrentUserId()));
        dataHasBeenChanged = true;
        return array;
    }

    public ArrayList<Group.Member> addMembers(){
        return null;////////////////////////////////////////////////////////////////////////
    }

    private void downloadListOfGroupsNames(){
        /*dataR.child("Db").child("Members").child(currentUserFirebaseId).child("list_of_groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Boolean> map = (Map<String, Boolean>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        groupArrayList.add(new Group(0,"zero", downloadGroup(),53.894810,27.509498,50));
    }

    private ArrayList<Group.Member> downloadGroup(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member(0,"Mikhail Zaitsev", Uri.parse("an"),false,"RdKxPTuHXRdNENmEd6vrg15dzTs1"));
        return array;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public boolean isDataHasBeenChanged() {
        return dataHasBeenChanged;
    }
    public void setDataHasBeenChanged(boolean dataHasBeenChanged) {
        this.dataHasBeenChanged = dataHasBeenChanged;
    }
    public boolean isOnlineHasBeenChanged() {
        return onlineHasBeenChanged;
    }
    public void setOnlineHasBeenChanged(boolean onlineHasBeenChanged) {
        this.onlineHasBeenChanged = onlineHasBeenChanged;
    }
}
