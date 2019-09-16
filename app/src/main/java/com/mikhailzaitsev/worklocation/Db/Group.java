package com.mikhailzaitsev.worklocation.Db;

import android.net.Uri;
import java.util.ArrayList;

public class Group {
    private String groupId;
    private String groupName;
    private ArrayList<Member> members;

    public void deleteGroupOrItem(ArrayList<Group> groupArrayList, int g){
        groupArrayList.remove(g);
    }
    public void deleteGroupOrItem(ArrayList<Group> groupArrayList, int g, int i){
        groupArrayList.get(g).members.remove(i);
    }

    public static ArrayList<Group> makeGroup(){
        ArrayList<Group> groupArrayList = new ArrayList<>();
        groupArrayList.add(new Group("000","zero",Member.makeMember()));
        groupArrayList.add(new Group("001","first",Member.makeMember()));
        groupArrayList.add(new Group("002","second",Member.makeMember()));
        groupArrayList.add(new Group("003","third",Member.makeMember()));
        return groupArrayList;
    }

    private Group(String groupId, String groupName, ArrayList<Member> members){
        setGroupId(groupId);
        setMembers(members);
        setGroupName(groupName);
    }

    public String getGroupId() {
        return groupId;
    }
    public String getGroupName() {
        return groupName;
    }
    public ArrayList<Member> getMembers() {
        return members;
    }
    public int getMemberSize(){
        return this.members.size();
    }

    private void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    private void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    private void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

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
