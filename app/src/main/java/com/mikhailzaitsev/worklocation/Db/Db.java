package com.mikhailzaitsev.worklocation.Db;

import android.net.Uri;
import android.util.SparseArray;

import com.google.android.gms.maps.model.Circle;

import java.util.ArrayList;
import java.util.Collections;

public class Db {
    private static ArrayList<Group> groupArrayList;


    public static void saveCircleChanges(ArrayList<Circle> circleArrayList){
        Circle circle;
        for (int i=0; i<circleArrayList.size(); i++ ){
            circle = circleArrayList.get(i);
            groupArrayList.get(i).setLongitude(circle.getCenter().longitude);
            groupArrayList.get(i).setLatitude(circle.getCenter().latitude);
            groupArrayList.get(i).setRadius((int)circle.getRadius());
        }
    }

    public static void deleteMember(SparseArray<ArrayList<Integer>> listOfChoosedItems){
        for (int i = 0; i<listOfChoosedItems.size(); i++){
            ArrayList<Integer> sortedList = listOfChoosedItems.get(i);
            Collections.sort(sortedList);
            Collections.reverse(sortedList);
            for (int j :sortedList){
                groupArrayList.get(i).getMembers().remove(j);
            }
        }

    }

    public static ArrayList<Group> createDb(){
        if (groupArrayList != null){
            return groupArrayList;
        }else {
            return makeGroup();
        }
    }

    private static ArrayList<Group> makeGroup(){
        groupArrayList = new ArrayList<>();
        groupArrayList.add(new Group("000","zero", makeMember(),53.897124,27.513986,15));
        groupArrayList.add(new Group("001","first", makeMember(),53.894841,27.509496,20));
        groupArrayList.add(new Group("002","second", makeMember(),53.897546,27.520649,40));
        groupArrayList.add(new Group("003","third", makeMember(),53.899595,27.515158,80));
        return groupArrayList;
    }

    private static ArrayList<Group.Member> makeMember(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member("0","mirt", Uri.parse("an"),true));
        array.add(new Group.Member("6","Tfg Lkcv", Uri.parse("antt"),true));
        array.add(new Group.Member("1","Oppp Sdfc", Uri.parse("an"),false));
        array.add(new Group.Member("2","Qwe trthh", Uri.parse("anq"),false));
        array.add(new Group.Member("3","Trytytr tty", Uri.parse("anw"),true));
        array.add(new Group.Member("5","Ter Tsd", Uri.parse("anrtrr"),false));
        array.add(new Group.Member("7","Zercf Ipopdfc", Uri.parse("anyy"),false));
        array.add(new Group.Member("4","Adfrtrey Trer", Uri.parse("aneere"),true));
        return array;
    }
}
