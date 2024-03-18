package com.ssafypjt.bboard.model.entity;

import lombok.*;

@Getter
@NoArgsConstructor
public class Group {
    private int id;
    private String groupName;
    @Setter
    private String password;
}
