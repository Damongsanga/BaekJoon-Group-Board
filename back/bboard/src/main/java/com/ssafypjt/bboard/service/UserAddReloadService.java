package com.ssafypjt.bboard.service;

import com.ssafypjt.bboard.model.domain.solvedacAPI.*;
import com.ssafypjt.bboard.model.entity.UserTier;
import com.ssafypjt.bboard.model.repository.*;
import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAddReloadService {

    private final ProblemRepository problemRepository;
    private final ProblemAlgorithmRepository problemAlgorithmRepository;
    private final UserTierProblemRepository userTierProblemRepository;
    private final TierProblemRepository tierProblemRepository;

    @Transactional
    public void resetProblems(List<ProblemAlgorithmVo> list) {
        Collections.sort(list);
        problemAlgorithmRepository.insertAlgorithms(list);
        problemRepository.insertProblems(list);

        log.info("problems added : {}", list.size());
        log.info("add problems are {}", list.size());
    }

    @Transactional
    public void resetUserTier(List<UserTier> list) {
        tierProblemRepository.insertUserTiers(list);
    }

    @Transactional
    public void resetUserTierProblems(List<ProblemAlgorithmVo> list) {
        problemAlgorithmRepository.insertAlgorithms(list);
        userTierProblemRepository.insertTierProblems(list);
    }


}
