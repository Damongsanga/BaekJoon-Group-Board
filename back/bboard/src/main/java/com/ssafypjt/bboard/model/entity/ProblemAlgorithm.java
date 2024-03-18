package com.ssafypjt.bboard.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProblemAlgorithm {
    private int problemNum;
    private String algorithm;
}
