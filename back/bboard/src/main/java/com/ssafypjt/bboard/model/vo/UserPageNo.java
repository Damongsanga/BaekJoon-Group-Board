package com.ssafypjt.bboard.model.vo;

import com.ssafypjt.bboard.model.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPageNo {

    private User user;
    private int pageNo;


    public UserPageNo(User user, int pageNo) {
        this.user = user;
        this.pageNo = pageNo;
    }


}
