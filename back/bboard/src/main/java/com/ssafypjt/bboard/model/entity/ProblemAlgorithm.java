package com.ssafypjt.bboard.model.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProblemAlgorithm {
    private int problemNum;
    private String algorithm;
}
