package com.ssafypjt.bboard.model.service;

import com.ssafypjt.bboard.model.entity.*;
import com.ssafypjt.bboard.model.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemAlgorithmRepository problemAlgorithmRepository;
    private final RecomProblemRepository recomProblemRepository;
    private final TierProblemRepository tierProblemRepository;
    private final UserTierProblemRepository userTierProblemRepository;

    @Override
    public List<Problem> getAllProblems() {
        return problemRepository.selectAllProblems();
    }

    @Override
    public Problem getProblem(int id) {
        return problemRepository.selectProblem(id);
    }

    @Override
    public Problem getProblemByNum(int problemNum) {
        return problemRepository.selectProblemByNum(problemNum);
    }

    @Override
    public List<UserTier> getAllUserTiers() {
        return tierProblemRepository.selectAllUserTiers();
    }

    @Override
    public List<UserTier> getUserTiers(User user) {
        return tierProblemRepository.selectUserTiers(user.getUserId());
    }


    @Override
    public List<Problem> getUserTierProblems(int userId) {
        return userTierProblemRepository.selectTierProblems(userId);
    }

    @Override
    @Transactional
    public int addRecomProblem(int problemId, int groupId, int userId) {
        Problem problem = problemRepository.selectProblem(problemId);
        problem.setUserId(userId);

        // 이미 같은 그룹에 해당 문제가 등록된 적이 있는지 확인
        if (recomProblemRepository.selectRecomProblem(problem.getProblemNum(), groupId) != null){
            return 0;
        }

        // 그룹별로 10개가 초과되면 id가 빠른 순 (등록이 빠른 순) 으로 삭제
        if (recomProblemRepository.selectGroupRecomProblems(groupId).size() >= 10){
            recomProblemRepository.deleteFirstRecomProblem();
        }

        return recomProblemRepository.insertRecomProblem(problem, groupId);
    }

    @Override
    public RecomProblem getRecomProblem(int problemNum, int groupId) {
        return recomProblemRepository.selectRecomProblem(problemNum, groupId);
    }

    @Override
    public List<RecomProblem> getAllRecomProblems() {
        return recomProblemRepository.selectAllRecomProblems();
    }

    @Override
    public List<ProblemAlgorithm> getAllAlgorithm() {
        return problemAlgorithmRepository.selectAllAlgorithms();
    }

    @Override
    public ProblemAlgorithm getProblemAlgorithm(int problemNum) {
        return problemAlgorithmRepository.selectAlgorithm(problemNum);
    }

}
