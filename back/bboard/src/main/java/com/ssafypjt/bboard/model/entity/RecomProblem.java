package com.ssafypjt.bboard.model.entity;

import lombok.*;

@Getter
@NoArgsConstructor
public class RecomProblem{
    private int id;
    private int userId;
    private int groupId;
    private int problemNum;
    private int tier;
    private int title;
}
