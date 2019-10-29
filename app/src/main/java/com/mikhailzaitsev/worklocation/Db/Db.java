package com.mikhailzaitsev.worklocation.Db;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

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

    public void deleteMemberById(String groupId, String memberId){
        try {
            int groupIndex = findGroupIndex(groupId);
            groupArrayList.get(groupIndex).getMembers().remove(findMemberIndex(groupIndex,memberId));
        }catch (Exception e){
            Log.e("TAG","ERROR at DB.deleteGroupById :" + e.getMessage());
        }
        dataHasBeenChanged = true;
    }

    public void deleteGroupById(String groupId){
        try {
            groupArrayList.remove(findGroupIndex(groupId));
        }catch (Exception e){
            Log.e("TAG","ERROR at DB.deleteGroupById :" + e.getMessage());
        }
        dataHasBeenChanged = true;
    }

    private int findMemberIndex(int groupIndex,String memberId){
        Long id = Long.valueOf(memberId);
        for (int i = 0; i < groupArrayList.get(groupIndex).getMemberSize(); i++) {
            if (id.equals(groupArrayList.get(groupIndex).getMembers().get(i).getMemberId())) {
                return i;
            }
        }
        return -1;
    }

    private int findGroupIndex(String groupId){
        Long id = Long.valueOf(groupId);
        for (int i = 0; i < groupArrayList.size(); i++) {
            if (id.equals(groupArrayList.get(i).getGroupId())) {
                return i;
            }
        }
        return -1;
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
                Math.round(radius + Calendar.getInstance().getTimeInMillis())/41
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
        groupArrayList.add(new Group(111,"1", downloadGroup(),53.894810,27.509498,50));
        groupArrayList.add(new Group(222,"2", downloadGroup1(),53.894810,27.509198,60));
        groupArrayList.add(new Group(333,"3", downloadGroup2(),53.894110,27.509900,70));
    }

    private ArrayList<Group.Member> downloadGroup(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member(1,"1", Uri.parse("an"),false,"RdKxfPTuHXRdNENmEd6vrg15dzTs1"));
        array.add(new Group.Member(2,"2", Uri.parse("and"),false,"asdasdsadasdENmEgd6vrg15dzyf"));
        array.add(new Group.Member(3,"3", Uri.parse("an2d"),false,"asdasdsadasdEzNmEd6vwwqrg15dzyf"));
        array.add(new Group.Member(4,"4", Uri.parse("awnd"),false,"asdasdsew3adaszdENmEd6vrg15dzyf"));
        return array;
    }
    private ArrayList<Group.Member> downloadGroup1(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member(5,"5", Uri.parse("an1"),false,"RdKxPTuHXRdNEcNmEd6vrg15dzgTs1"));
        array.add(new Group.Member(6,"6", Uri.parse("and2"),false,"asdasdsadasvdENmEd6vrg15fdzyf"));
        array.add(new Group.Member(7,"7", Uri.parse("an2d3"),false,"asdasdsadhasdENmEd2116dvwwqrg15dzyf"));
        array.add(new Group.Member(8,"8", Uri.parse("awnd4"),false,"asdasdse7w3adasdEN1mE2d6vbrg15dzyf"));
        return array;
    }
    private ArrayList<Group.Member> downloadGroup2(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member(9,"9", Uri.parse("an5"),false,"RdKxPTuHX9RdNENm56Ed6vrg15dzTs1"));
        array.add(new Group.Member(10,"10", Uri.parse("an6d"),false,"asdas7dsadasdE3Nm5Ed6vrg15dzyf"));
        array.add(new Group.Member(11,"11", Uri.parse("an72d"),false,"asd1asdsadas2dENm7Ed6vwwqrg15dzyf"));
        array.add(new Group.Member(12,"12", Uri.parse("aw8nd"),false,"as2dasdsew31adasdE8NmEd6vrg15dzyf"));
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
