package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafypjt.bboard.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDomain {

    private ObjectMapper mapper;

    private final List<User> userList = new ArrayList<>();

    public List<User> getUserList(){
        return userList;
    }

    @Autowired
    public UserDomain(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public User makeUserObject(JsonNode aNode) {
        User user = mapper.convertValue(aNode, User.class);
        if (user.getImgUrl() == null) {
            int no = (int) (Math.random() * 10);
            user.setImgUrl("http://localhost:8080/images/default_image_" + no + ".jpg");
        }
        return user;
    }

}
