package com.ssafypjt.bboard.model.service;

import com.ssafypjt.bboard.model.dto.Group;

import java.util.List;

// 그룹 관리 관련된 내용만 정의해보자
public interface GroupService {
    // 그룹 가져오기
    public Group getGroup(int groupId);

    public List<Group> getGroups(int userId);

    // 그룹 생성, 유저의 그룹 수 가져와서 3개이면 못만들게 제한
    public int makeGroup(Group group);

    // 그룹 삭제, adminValid 필요
    public int removeGroup(int groupId);

    // 관리자가 유저 등록시키기, adminValid 필요
    public int addUser(Group group, int userId);

    // 관리자가 유저 탈퇴시키기, adminValid 필요
    public int removeUser(int groupId, int userId);

    // 관리자 기능 사용 가능한지 비밀번호로 확인
    public boolean adminValid(int id, String password);



//    // 그룹 이름 변경, adminValid 필요
//    public int changeGroupName(int id, String password, String newName);
//      // 그룹 비밀번호 변경, adminValid 필요
//      public int changePassword(int id, String password, String NewPassword);

}
