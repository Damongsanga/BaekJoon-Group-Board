package com.ssafypjt.bboard.service;

import com.ssafypjt.bboard.model.entity.*;

import java.util.List;

public interface ProblemService {

    // 총 문제 리스트 가져오기
    public List<Problem> getAllProblems();

    // 문제 가져오기
    public Problem getProblem(int id);

    public Problem getProblemByNum(int problemNum);

    // user-tier, user-tier problem 로직

    // 등록된 유저들의 유저-티어 정보를 가져오기 (필요한가?)
    public List<UserTier> getAllUserTiers();

    // 유저 1명의 유저-티어정보 가져오기
    public List<UserTier> getUserTiers(User user);

    // 한 유저(로그인 한 유저)에게 추천할 유저-티어-문제들을 가져오기
    public List<Problem> getUserTierProblems(int userId);

    // recom_problem 로직
    public int addRecomProblem(int problemId, int groupId, int userId);

    public RecomProblem getRecomProblem(int problemNum, int groupId);

    public List<RecomProblem> getAllRecomProblems();

    // front 단에서 String Parsing 필요
    public List<ProblemAlgorithm> getAllAlgorithm();

    // 한 문제에 연관된 알고리즘을 가져오기
    public ProblemAlgorithm getProblemAlgorithm(int problemNum);

}
