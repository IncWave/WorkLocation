package com.mikhailzaitsev.worklocation.Db;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Group implements Parcelable {
    private int radius;
    private String groupId;
    private String groupName;
    private double latitude;
    private double longitude;
    private ArrayList<Member> members;

    public void deleteGroupOrItem(ArrayList<Group> groupArrayList, int g){
        groupArrayList.remove(g);
    }
    public void deleteGroupOrItem(ArrayList<Group> groupArrayList, int g, int i){
        groupArrayList.get(g).members.remove(i);
    }

    public static ArrayList<Group> makeGroup(){
        ArrayList<Group> groupArrayList = new ArrayList<>();
        groupArrayList.add(new Group("000","zero",Member.makeMember(),53.897124,27.513986,15));
        groupArrayList.add(new Group("001","first",Member.makeMember(),53.894841,27.509496,20));
        groupArrayList.add(new Group("002","second",Member.makeMember(),53.897546,27.520649,40));
        groupArrayList.add(new Group("003","third",Member.makeMember(),53.899595,27.515158,80));
        return groupArrayList;
    }

    private Group(String groupId, String groupName, ArrayList<Member> members,double latitude, double longitude,int radius){
        setGroupId(groupId);
        setGroupName(groupName);
        setMembers(members);
        setLatitude(latitude);
        setLongitude(longitude);
        setRadius(radius);
    }

    private Group(String groupName, double latitude, double longitude, int radius){
        this.groupName = groupName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
    public String getGroupId() {
        return groupId;
    }
    public double getLatitude() {
        return latitude;
    }
    public String getGroupName() {
        return groupName;
    }
    public double getLongitude() {
        return longitude;
    }
    public ArrayList<Member> getMembers() {
        return members;
    }
    public int getMemberSize(){
        return this.members.size();
    }

    private void setRadius(int radius) {
        this.radius = radius;
    }
    private void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    private void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    private void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    private void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    private void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(groupName);
        parcel.writeInt(radius);
        parcel.writeValue(latitude);
        parcel.writeValue(longitude);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel parcel) {
            String groupName = parcel.readString();
            int radius = parcel.readInt();
            double latitude = parcel.readDouble();
            double longitude = parcel.readDouble();
            return new Group(groupName, latitude, longitude, radius);
        }

        @Override
        public Group[] newArray(int i) {
            return new Group[0];
        }
    };

    public static class Member {
        private boolean memberIsOnline;
        private String memberName;
        private String memberId;
        private Uri memberUri;

        static private ArrayList<Member> makeMember(){
            ArrayList<Member> array = new ArrayList<>();
            array.add(new Member("0","mirt", Uri.parse("an"),true));
            array.add(new Member("6","Tfg Lkcv", Uri.parse("antt"),true));
            array.add(new Member("1","Oppp Sdfc", Uri.parse("an"),false));
            array.add(new Member("2","Qwe trthh", Uri.parse("anq"),false));
            array.add(new Member("3","Trytytr tty", Uri.parse("anw"),true));
            array.add(new Member("5","Ter Tsd", Uri.parse("anrtrr"),false));
            array.add(new Member("7","Zercf Ipopdfc", Uri.parse("anyy"),false));
            array.add(new Member("4","Adfrtrey Trer", Uri.parse("aneere"),true));
            return array;
        }

        private Member(String memberId, String memberName, Uri memberUri, boolean memberIsOnline){
            setMemberName(memberName);
            setOnline(memberIsOnline);
            setMemberUri(memberUri);
            setMemberId(memberId);
        }

        public boolean isOnline() {
            return memberIsOnline;
        }
        public String getMemberName() {
            return memberName;
        }
        public String getMemberId() {
            return memberId;
        }
        public Uri getMemberUri() {
            return memberUri;
        }

        private void setOnline(boolean memberIsOnline) {
            this.memberIsOnline = memberIsOnline;
        }
        private void setMemberName(String memberName) {
            this.memberName = memberName;
        }
        private void setMemberUri(Uri memberUri) {
            this.memberUri = memberUri;
        }
        private void setMemberId(String memberId) {
            this.memberId = memberId;
        }
    }
}
