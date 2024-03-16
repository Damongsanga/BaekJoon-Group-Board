package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.ssafypjt.bboard.model.entity.Problem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProblemAlgorithm implements Comparable<ProblemAlgorithm> {

    private Problem problem;
    private com.ssafypjt.bboard.model.entity.ProblemAlgorithm problemAlgorithm;

    public ProblemAlgorithm(Problem problem, com.ssafypjt.bboard.model.entity.ProblemAlgorithm problemAlgorithm) {
        this.problem = problem;
        this.problemAlgorithm = problemAlgorithm;
    }

    @Override
    public int compareTo(ProblemAlgorithm o) {
        if (this.problem.getTier() > o.problem.getTier()) {
            return 1;
        } else if (this.problem.getTier() < o.problem.getTier()) {
            return -1;
        }
        return 0;
    }

}
