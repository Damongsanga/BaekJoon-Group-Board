package com.ssafypjt.bboard.service;

import com.ssafypjt.bboard.model.entity.Group;
import com.ssafypjt.bboard.model.entity.User;

import java.util.List;

// 그룹 관리 관련된 내용만 정의해보자
public interface GroupService {
    // 그룹 가져오기
    public Group getGroup(int groupId);

    public Group getGroupByName(String groupName);

    public List<Group> getGroups(int userId);

    // 그룹 생성, 유저의 그룹 수 가져와서 3개이면 못만들게 제한
    public int makeGroup(int userId, Group group);

    // 그룹 삭제, adminValid 필요
    public int removeGroup(int groupId);

    public List<User> getUsers(int groupId);

    // 관리자가 유저 등록시키기, adminValid 필요
    public User addUser(int groupId, int userId);

    // 관리자가 유저 탈퇴시키기, adminValid 필요
    public int removeUser(int groupId, int userId);

    // 관리자 기능 사용 가능한지 비밀번호로 확인
    public boolean adminValid(int id, String password);
}
