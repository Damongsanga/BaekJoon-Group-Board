package com.ssafypjt.bboard.model.domain.groupinfo;

import com.ssafypjt.bboard.model.entity.Group;
import com.ssafypjt.bboard.model.entity.User;
import lombok.Data;

@Data
public class UserGroup {

    private User user;
    private Group group;

    public UserGroup(User user, Group group) {
        this.user = user;
        this.group = group;
    }

    public UserGroup() {
    }
}
