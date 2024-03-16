package com.ssafypjt.bboard.model.service;

import com.ssafypjt.bboard.model.entity.Group;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.repository.GroupRepository;
import com.ssafypjt.bboard.model.repository.RecomProblemRepository;
import com.ssafypjt.bboard.model.repository.UserGroupRepository;
import com.ssafypjt.bboard.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService{

    private GroupRepository groupRepository;
    private UserRepository userRepository;
    private UserGroupRepository userGroupRepository;
    private RecomProblemRepository recomProblemRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, UserGroupRepository userGroupRepository, RecomProblemRepository recomProblemRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.recomProblemRepository = recomProblemRepository;
    }

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
        for (Integer groupId : userGroupRepository.selectGroupId(userId)) {
            list.add(groupRepository.selectGroup(groupId));
        }
        return list;
    }

    @Override
    public int makeGroup(User user, Group group) {
        if (groupRepository.selectGroupByName(group.getGroupName()) != null) return -1;
        if (userGroupRepository.selectGroupId(user.getUserId()).size() >= 3) return 0; // 해당 그룹이 3개 이하일 때만
        return groupRepository.insertGroup(group);
    }

    // 그룹 삭제시 그룹 내 유저 전부 탈퇴, 그룹내 유저추천 문제 삭제 동시 진행
    @Override
    @Transactional
    public int removeGroup(int groupId) {
        userGroupRepository.removeAllUserGroup(groupId);
        recomProblemRepository.deleteGroupRecomProblemd(groupId);
        return groupRepository.deleteGroup(groupId);
    }

    public List<User> getUsers(int groupId){
        List<User> userList = new ArrayList<>();
        for(int userId : userGroupRepository.selectUserId(groupId)){
            userList.add(userRepository.selectUser(userId));
        }
        return userList;
    }

    @Override
    public int addUser(Group group, int userId) { // 유저 등록 & 유저-그룹 관계 등록
        int groupId = group.getId();
        if (groupId == 0) { // 새로운 그룹이라면
            groupId = groupRepository.selectGroupByName(group.getGroupName()).getId();
        }
        return userGroupRepository.insertUserGroup(userId, groupId);
    }

    @Override
    public int removeUser(int groupId, int userId) {
        return userGroupRepository.removeUserGroup(userId, groupId);
    }

    @Override
    public boolean adminValid(int id, String password) {
        return groupRepository.selectGroupByPassword(id, password) != null;
    }
}
