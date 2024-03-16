package com.ssafypjt.bboard.model.domain.groupinfo;

import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.ProblemAlgorithm;
import com.ssafypjt.bboard.model.entity.RecomProblem;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GroupDomain {

    private final ProblemAlgorithmRepository problemAlgorithmRepository;
    private final ProblemRepository problemRepository;
    private final RecomProblemRepository recomProblemRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final UserTierProblemRepository userTierProblemRepository;

    private final int TIER_DIFF_UPPERBOUND = 5;
    private final int TIER_DIFF_LOWERBOUND = -2;

    @Autowired
    public GroupDomain(ProblemAlgorithmRepository problemAlgorithmRepository, ProblemRepository problemRepository, RecomProblemRepository recomProblemRepository, UserGroupRepository userGroupRepository, UserRepository userRepository, UserTierProblemRepository userTierProblemRepository) {
        this.problemAlgorithmRepository = problemAlgorithmRepository;
        this.problemRepository = problemRepository;
        this.recomProblemRepository = recomProblemRepository;
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
        this.userTierProblemRepository = userTierProblemRepository;
    }

    public List<User> getUsers(UserGroup userAndGroup){
        List<User> userList = new ArrayList<>();
        for(int userId : userGroupRepository.selectUserId(userAndGroup.getGroup().getId())){
            userList.add(userRepository.selectUser(userId));
        }
        return userList;
    }

    // 반출 순서는 1. 티어 내림차순 2. 문제수 오름차순 (2번 조건을 안주면 userId가별로 오름차순되어서 userId가 낮을 수록 유리해진다)
    public List<Problem> getProblems(UserGroup userAndGroup, List<User> userList){
        List<Problem> problemList = problemRepository.selectGroupProblem(userList);
        Collections.sort(problemList, (o1, o2) -> {
            if (o2.getTier() == o1.getTier()) return o1.getProblemNum() - o2.getProblemNum();
            return o2.getTier() - o1.getTier();
        });
        List<Problem> returnList = new ArrayList<>();
        // 100개 선정 로직
        Set<Integer> set = new HashSet<>();
        int idx = 0;
        while (set.size() <= 100) {
            if (idx >= problemList.size()) break;
            Problem problem = problemList.get(idx++);
            if (set.add(problem.getProblemNum())) {
                if (set.size() > 100) break;
            }
            returnList.add(problem);
        }

        return returnList;

    }

    // 로그인된 유저와 관련된 문제만 선정
    // 로그인된 유저 레벨과 아래로 2, 위로 5차이나는 문제 가져오기
    // 다 주고 프론트에서 랜덤으로 추천해주는 것도 좋을듯?! -> 안좋음, 백에서 다 정리해서 주자
    public List<Problem> getUserTierProblems(UserGroup userAndGroup, List<User> userList){
        List<Problem> returnList = new ArrayList<>();
        Map<Integer, Problem> map = new HashMap<>();
        User user = userAndGroup.getUser();

        for (Problem problem: userTierProblemRepository.selectGroupTierProblem(userList)) {
            int tierDiff = problem.getTier() - user.getTier();
            if (TIER_DIFF_LOWERBOUND <= tierDiff && tierDiff <= TIER_DIFF_UPPERBOUND) {
                map.putIfAbsent(problem.getUserId(), problem);
                int mapTierDiff = map.get(problem.getUserId()).getTier() - user.getTier();
                if (Math.abs(tierDiff) < Math.abs(mapTierDiff)){
                    map.put(problem.getUserId(), problem);
                }
            }
        }

        returnList.addAll(map.values());
        return returnList;
    }

    public List<RecomProblem> getRecomProblems(UserGroup userAndGroup){
        return recomProblemRepository.selectGroupRecomProblems(userAndGroup.getGroup().getId());
    }

    // 그룹의 모든 문제의 알고리즘 정보 선정
    // user_group과 problem_algorithm 테이블을 연동하기 위해서는 join을 3번해야해서 모든 알고리즘을 가져오고 이분 탐색을 사용하였다.
    public List<ProblemAlgorithm> getAlgorithms(List<Problem> top100problemList, List<RecomProblem> recomProblemList){
        List<ProblemAlgorithm> problemAlgorithms = problemAlgorithmRepository.selectAllAlgorithm(); // 문제 번호로 오름차순 정렬
        int[] problemNums = new int[problemAlgorithms.size()];
        for (int i = 0; i< problemAlgorithms.size(); i++){
            problemNums[i] = problemAlgorithms.get(i).getProblemNum();
        }

        // BinarySearch / 이분탐색
        List<ProblemAlgorithm> returnList = new ArrayList<>();
        for (Problem problem : top100problemList){
            returnList.add(problemAlgorithms.get(Arrays.binarySearch(problemNums, problem.getProblemNum())));
        }
        for (RecomProblem recomProblem : recomProblemList){
            returnList.add(problemAlgorithms.get(Arrays.binarySearch(problemNums, recomProblem.getProblemNum())));
        }

        return returnList;
    }

    public List<Problem> getUserProblems(UserGroup userAndGroup){
        return problemRepository.selectUserProblem(userAndGroup.getUser().getUserId());
    }


}
