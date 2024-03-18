package com.ssafypjt.bboard.model.vo;

import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.ProblemAlgorithm;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProblemAlgorithmVo implements Comparable<ProblemAlgorithmVo> {

    private Problem problem;
    private ProblemAlgorithm problemAlgorithm;

    public ProblemAlgorithmVo(Problem problem, com.ssafypjt.bboard.model.entity.ProblemAlgorithm problemAlgorithm) {
        this.problem = problem;
        this.problemAlgorithm = problemAlgorithm;
    }

    @Override
    public int compareTo(ProblemAlgorithmVo o) {
        if (this.problem.getTier() > o.problem.getTier()) {
            return 1;
        } else if (this.problem.getTier() < o.problem.getTier()) {
            return -1;
        }
        return 0;
    }

}
