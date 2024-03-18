package com.ssafypjt.bboard.model.service;

import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.repository.UserGroupRepository;
import com.ssafypjt.bboard.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public User getUser(int userId) {
        return userRepository.selectUser(userId);
    }

    @Override
    public User getUserByName(String userName) {
        return userRepository.selectUserByName(userName);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.selectAllUser();
    }


    @Override
    public List<Integer> getGroupIdByUser(int userId) {
        return userGroupRepository.selectGroupId(userId);
    }

}
