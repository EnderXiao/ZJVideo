package com.example.trtc_client;

import java.util.ArrayList;
import java.util.List;

public class AnswerActivity extends MainActivity {

    public static List<StudentDataBean> joinList = new ArrayList<>();
    public static List<ClassDataBean> ketangList = new ArrayList<>();
    public static List<MemberDataBean> handsUpList = new ArrayList<>();

    public static StudentDataBean findMemberInJoinList(String userId) {
        for (int i = 0; i < joinList.size(); i++ ){
            if(userId.equals(joinList.get(i).getUserId()))
                return joinList.get(i);
        }
        return null;
    }

    public static ClassDataBean findMemberInKetangList(String userId) {
        for (int i = 0; i < ketangList.size(); i++ ){
            if(userId.equals(ketangList.get(i).getUserId()))
                return ketangList.get(i);
        }
        return null;
    }
}
