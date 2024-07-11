package com.ssafypjt.bboard.service;

import com.ssafypjt.bboard.model.entity.Group;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.repository.GroupRepository;
import com.ssafypjt.bboard.model.repository.RecomProblemRepository;
import com.ssafypjt.bboard.model.repository.UserGroupRepository;
import com.ssafypjt.bboard.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final RecomProblemRepository recomProblemRepository;

    @Override
    public Group getGroup(int groupId) {
        return groupRepository.selectGroup(groupId);
    }

    @Override
    public Group getGroupByName(String groupName) {
        return groupRepository.selectGroupByName(groupName);
    }

    @Override
    public List<Group> getGroups(int userId) {
        List<Group> list = new ArrayList<>();
        for (Integer groupId : userGroupRepository.selectGroupIds(userId)) {
            list.add(groupRepository.selectGroup(groupId));
        }
        return list;
    }

    @Override
    @Transactional
    public int makeGroup(int userId, Group group) {
        if (groupRepository.selectGroupByName(group.getGroupName()) != null) return -1;
        if (userGroupRepository.selectGroupIds(userId).size() >= 3) return 0; // 해당 그룹이 3개 이하일 때만
        return groupRepository.insertGroup(group);
    }

    // 그룹 삭제시 그룹 내 유저 전부 탈퇴, 그룹내 유저추천 문제 삭제 동시 진행
    @Override
    @Transactional
    public int removeGroup(int groupId) {
        userGroupRepository.removeAllUserGroup(groupId);
        recomProblemRepository.deleteGroupRecomProblem(groupId);
        return groupRepository.deleteGroup(groupId);
    }

    public List<User> getUsers(int groupId){
        List<User> userList = new ArrayList<>();
        for(int userId : userGroupRepository.selectUserIds(groupId)){
            userList.add(userRepository.selectUser(userId));
        }
        return userList;
    }

    @Override
    @Transactional
    public User addUser(int groupId, int userId) { // 유저 등록 & 유저-그룹 관계 등록
//        if (groupId == 0) { // 새로운 그룹이라면
//            groupId = groupRepository.selectGroupByName(group.getGroupName()).getId();
//        } // 불필요한것 같음 확인 필요
        userGroupRepository.insertUserGroup(userId, groupId);
        return userRepository.selectUser(userId);
    }

    @Override
    @Transactional
    public int removeUser(int groupId, int userId) {
        return userGroupRepository.removeUserGroup(userId, groupId);
    }

    public boolean adminValid(int id, String password) {
        return groupRepository.selectGroupByPassword(id, password) != null;
    }
}
