package com.ssafypjt.bboard.model.entity;

import com.ssafypjt.bboard.model.entity.Group;
import com.ssafypjt.bboard.model.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserGroup {

    private User user;
    private Group group;

    public UserGroup(User user, Group group) {
        this.user = user;
        this.group = group;
    }
}
