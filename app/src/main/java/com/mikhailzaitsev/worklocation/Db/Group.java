package com.mikhailzaitsev.worklocation.Db;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;


public class Group implements Parcelable{
    private int radius;
    private long groupId;
    private String groupName;
    private double latitude;
    private double longitude;
    private ArrayList<Member> members;


    Group(long groupId, String groupName, ArrayList<Member> members,double latitude, double longitude,int radius){
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
    public long getGroupId() {
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


    void setRadius(int radius) {
        this.radius = radius;
    }
    void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    void setMembers(ArrayList<Member> members) {
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
        private long memberId;
        private Uri memberUri;

        Member(long memberId, String memberName, Uri memberUri, boolean memberIsOnline){
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
        public long getMemberId() {
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
        private void setMemberId(long memberId) {
            this.memberId = memberId;
        }
    }
}
