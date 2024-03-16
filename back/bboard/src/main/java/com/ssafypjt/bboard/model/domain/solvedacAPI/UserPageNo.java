package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.ssafypjt.bboard.model.entity.User;
import lombok.Data;

@Data
public class UserPageNo {


    private User user;
    private int pageNo;

    public UserPageNo() {
    }

    public UserPageNo(User user, int pageNo) {
        this.user = user;
        this.pageNo = pageNo;
    }


}
