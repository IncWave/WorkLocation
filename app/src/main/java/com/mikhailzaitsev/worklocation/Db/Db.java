package com.mikhailzaitsev.worklocation.Db;

import android.net.Uri;
import android.util.SparseArray;

import com.google.android.gms.maps.model.Circle;

import java.util.ArrayList;
import java.util.Collections;


public class Db {
    private static ArrayList<Group> groupArrayList;
    private static Db db;

    private Db() {
        groupArrayList = new ArrayList<>();
        makeGroup();
    }

    public ArrayList<Group> getGroupArray(){
        return groupArrayList;
    }

    public static Db newInstance() {
        //Bundle args = new Bundle();
        //Db fragment = new Db();
        //fragment.setArguments(args);
        if (db == null){
            db = new Db();
            return db;
        }else { return db; }
    }

    public void saveCircleChanges(ArrayList<Circle> circleArrayList){
        Circle circle;
        for (int i=0; i<circleArrayList.size(); i++ ){
            circle = circleArrayList.get(i);
            groupArrayList.get(i).setLongitude(circle.getCenter().longitude);
            groupArrayList.get(i).setLatitude(circle.getCenter().latitude);
            groupArrayList.get(i).setRadius((int)circle.getRadius());
        }
    }

    public void deleteMember(SparseArray<ArrayList<Integer>> listOfChoosedItems){
        int listSize = listOfChoosedItems.size();
        int i = 0;
        while (listSize != 0){
            if (listOfChoosedItems.get(i) == null) {
                i++;
                continue;
            }
            ArrayList<Integer> sortedList = listOfChoosedItems.get(i);
            Collections.sort(sortedList);
            Collections.reverse(sortedList);
            for (int j : sortedList) {
                groupArrayList.get(i).getMembers().remove(j);
            }
            listSize--;
        }
    }

    private void makeGroup(){
        groupArrayList.add(new Group(0,"zero", makeMember(),53.897124,27.513986,15));
        groupArrayList.add(new Group(100,"first", makeMember1(),53.894841,27.509496,20));
        groupArrayList.add(new Group(200,"second", makeMember(),53.897546,27.520649,40));
        groupArrayList.add(new Group(300,"third", makeMember1(),53.899595,27.515158,80));
    }

    private ArrayList<Group.Member> makeMember(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member(0,"mirt", Uri.parse("an"),true));
        array.add(new Group.Member(123,"Tfg Lkcv", Uri.parse("antt"),true));
        array.add(new Group.Member(235,"Oppp Sdfc", Uri.parse("an"),false));
        array.add(new Group.Member(3678,"Qwe trthh", Uri.parse("anq"),false));
        array.add(new Group.Member(465,"Trytytr tty", Uri.parse("anw"),true));
        array.add(new Group.Member(578,"Ter Tsd", Uri.parse("anrtrr"),false));
        array.add(new Group.Member(6,"Zercf Ipopdfc", Uri.parse("anyy"),false));
        array.add(new Group.Member(798,"Adfrtrey Trer", Uri.parse("aneere"),true));
        array.add(new Group.Member(232,"111", Uri.parse("an"),true));
        array.add(new Group.Member(1223,"2222", Uri.parse("anstt"),true));
        array.add(new Group.Member(2335,"3333c", Uri.parse("asn"),false));
        array.add(new Group.Member(36478,"4444", Uri.parse("adnq"),false));
        array.add(new Group.Member(4655,"5555", Uri.parse("anfw"),true));
        array.add(new Group.Member(5768,"6666", Uri.parse("andrtrr"),false));
        array.add(new Group.Member(67,"7777", Uri.parse("anayy"),false));
        array.add(new Group.Member(7898,"8888", Uri.parse("anesere"),true));
        return array;
    }
    private ArrayList<Group.Member> makeMember1(){
        ArrayList<Group.Member> array = new ArrayList<>();
        array.add(new Group.Member(85,"wwww", Uri.parse("an"),true));
        array.add(new Group.Member(85,"pppp", Uri.parse("anp"),true));
        array.add(new Group.Member(933,"eree Lkcv", Uri.parse("antt"),true));
        array.add(new Group.Member(107,"tttt Sdfc", Uri.parse("an"),false));
        array.add(new Group.Member(111,"yyyyy trthh", Uri.parse("anq"),false));
        array.add(new Group.Member(121,"uuuuuu tty", Uri.parse("anw"),true));
        array.add(new Group.Member(1023,"dddd Tsd", Uri.parse("anrtrr"),false));
        array.add(new Group.Member(141,"xxx Ipopdfc", Uri.parse("anyy"),false));
        array.add(new Group.Member(151,"zzzz Trer", Uri.parse("aneere"),true));
        array.add(new Group.Member(3432,"21", Uri.parse("an"),true));
        array.add(new Group.Member(1233,"23", Uri.parse("antt"),true));
        array.add(new Group.Member(2345,"O43", Uri.parse("an"),false));
        array.add(new Group.Member(35678,"45hh", Uri.parse("anq"),false));
        array.add(new Group.Member(4665,"T65r tty", Uri.parse("anw"),true));
        array.add(new Group.Member(57878,"T76", Uri.parse("anrtrr"),false));
        array.add(new Group.Member(86,"Zer78c", Uri.parse("anyy"),false));
        array.add(new Group.Member(7998,"A38Trer", Uri.parse("aneere"),true));
        array.add(new Group.Member(672,"161", Uri.parse("an"),true));
        array.add(new Group.Member(12543,"22622", Uri.parse("anstt"),true));
        array.add(new Group.Member(23435,"33373c", Uri.parse("a6sn"),false));
        array.add(new Group.Member(336478,"44444", Uri.parse("a5dnq"),false));
        array.add(new Group.Member(46555,"55555", Uri.parse("an4fw"),true));
        array.add(new Group.Member(56768,"67666", Uri.parse("and3rtrr"),false));
        array.add(new Group.Member(677,"77727", Uri.parse("ana2yy"),false));
        array.add(new Group.Member(78498,"88588", Uri.parse("anese3re"),true));
        array.add(new Group.Member(85,"www2w", Uri.parse("a4n"),true));
        array.add(new Group.Member(85,"pp3pp", Uri.parse("an34p"),true));
        array.add(new Group.Member(933,"ere7e Lkcv", Uri.parse("an2tt"),true));
        array.add(new Group.Member(107,"ttt4t Sdfc", Uri.parse("a4n"),false));
        array.add(new Group.Member(111,"yyyy9y trthh", Uri.parse("an3q"),false));
        array.add(new Group.Member(121,"uuuuu3u tty", Uri.parse("an2w"),true));
        array.add(new Group.Member(1023,"ddd9d Tsd", Uri.parse("anrt3rr"),false));
        array.add(new Group.Member(141,"xxx3 Ipopdfc", Uri.parse("an4yy"),false));
        array.add(new Group.Member(151,"zzz0z Trer", Uri.parse("ane3ere"),true));
        array.add(new Group.Member(232,"321", Uri.parse("an"),true));
        array.add(new Group.Member(1233,"233", Uri.parse("a5ntt"),true));
        array.add(new Group.Member(2345,"O403", Uri.parse("a3n"),false));
        array.add(new Group.Member(35678,"452hh", Uri.parse("a4nq"),false));
        array.add(new Group.Member(4665,"T65r8 tty", Uri.parse("an3w"),true));
        array.add(new Group.Member(57878,"T796", Uri.parse("an5rtrr"),false));
        array.add(new Group.Member(86,"Zer378c", Uri.parse("an4yy"),false));
        array.add(new Group.Member(7998,"A838Trer", Uri.parse("ane3ere"),true));
        array.add(new Group.Member(672,"1361", Uri.parse("a4n"),true));
        array.add(new Group.Member(12543,"226522", Uri.parse("an7stt"),true));
        array.add(new Group.Member(23435,"333713c", Uri.parse("as7n"),false));
        array.add(new Group.Member(336478,"444474", Uri.parse("ad6nq"),false));
        array.add(new Group.Member(46555,"555955", Uri.parse("an5fw"),true));
        array.add(new Group.Member(56768,"676866", Uri.parse("an4drtrr"),false));
        array.add(new Group.Member(677,"777727", Uri.parse("ana3yy"),false));
        array.add(new Group.Member(78498,"885588", Uri.parse("anese4re"),true));
        return array;
    }
}
