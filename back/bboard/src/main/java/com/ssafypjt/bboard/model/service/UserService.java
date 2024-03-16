package com.ssafypjt.bboard.model.service;

import com.ssafypjt.bboard.model.entity.User;

import java.util.List;

public interface UserService {

    // 유저 가져오기
    public User getUser(int id);

    public User getUserByName(String userName);

    // 유저 전부 가져오기
    public List<User> getAllUser();

    public List<Integer> getGroupIdByUser(int userId);


}
