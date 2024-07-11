package com.ssafypjt.bboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafypjt.bboard.model.entity.*;
import com.ssafypjt.bboard.model.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupDataService {

    private final ProblemAlgorithmRepository problemAlgorithmRepository;
    private final ProblemRepository problemRepository;
    private final RecomProblemRepository recomProblemRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final UserTierProblemRepository userTierProblemRepository;
    private final ObjectMapper mapper;

    public ObjectNode getData(UserGroup userGroup){
        List<User> userList = getUsers(userGroup); // 그룹 해당 유저 정보
        List<Problem> top100problemList = getProblems(userList); // 그룹별 top 100개 문제 정보
        List<Problem> userTierProblemList = getUserTierProblems(userGroup, userList); // 그룹별 로그인된 유저에 해당하는 userTierProblem 정보
        List<RecomProblem> recomProblemList = getRecomProblems(userGroup); // 그룹별 recomProblem 정보
        List<ProblemAlgorithm> algorithmList = getAlgorithms(top100problemList, recomProblemList); // 그룹의 모든 문제의 알고리즘 정보
        List<Problem> userTop100ProblemList = getUserProblems(userGroup); // 유저의 top 100개 문제 정보 (Recomproblem 등록 위해)

        ObjectNode responseJson = JsonNodeFactory.instance.objectNode();
        responseJson.set("user", mapper.valueToTree(userGroup.getUser()));
        responseJson.set("group", mapper.valueToTree(userGroup.getGroup()));
        responseJson.set("users", mapper.valueToTree(userList));
        responseJson.set("top100problems", mapper.valueToTree(top100problemList));
        responseJson.set("userTierProblems", mapper.valueToTree((userTierProblemList)));
        responseJson.set("recomProblems", mapper.valueToTree(recomProblemList));
        responseJson.set("algorithms", mapper.valueToTree(algorithmList));
        responseJson.set("userTop100problems", mapper.valueToTree(userTop100ProblemList));

        return responseJson;
    }

    private List<User> getUsers(UserGroup userGroup){
        return userGroupRepository.selectUserIds(userGroup.getGroup().getId())
                .stream()
                .map(userRepository::selectUser)
                .toList();
    }

    // 반출 순서는 1. 티어 내림차순 2. 문제수 오름차순 (2번 조건을 안주면 userId가별로 오름차순되어서 userId가 낮을 수록 유리해진다)
    private List<Problem> getProblems(List<User> userList){
        List<Problem> problemList = problemRepository.selectGroupProblems(userList);
        problemList.sort((o1, o2) -> {
            if (o2.getTier() == o1.getTier()) return o1.getProblemNum() - o2.getProblemNum();
            return o2.getTier() - o1.getTier();
        });

        // 100개 선정 로직
        List<Problem> returnList = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        int idx = 0;
        while (set.size() <= 100) {
            if (idx >= problemList.size())
                break;

            Problem problem = problemList.get(idx++);
            if (set.add(problem.getProblemNum())) {
                if (set.size() > 100) break;
            }
            returnList.add(problem);
        }

        return returnList;

    }

    /**
     * 로그인된 유저와 관련된 문제만 선정
     * 로그인된 유저 레벨과 아래로 2, 위로 5차이나는 문제 가져오기
     * */
    private List<Problem> getUserTierProblems(UserGroup userGroup, List<User> userList){
        Map<Integer, Problem> map = new HashMap<>();
        User user = userGroup.getUser();
        int TIER_DIFF_UPPERBOUND = 5;
        int TIER_DIFF_LOWERBOUND = -2;

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

        return new ArrayList<>(map.values());
    }

    private List<RecomProblem> getRecomProblems(UserGroup userGroup){
        return recomProblemRepository.selectGroupRecomProblems(userGroup.getGroup().getId());
    }

    /**
     * 그룹의 모든 문제의 알고리즘 정보 선정
     * user_group과 problem_algorithm 테이블을 연동하기 위해서는 join을 3번해야해서 모든 알고리즘을 가져오고 이분 탐색을 사용
     * */
    private List<ProblemAlgorithm> getAlgorithms(List<Problem> top100problemList, List<RecomProblem> recomProblemList){
        List<ProblemAlgorithm> problemAlgorithms = problemAlgorithmRepository.selectAllAlgorithms(); // 문제 번호로 오름차순 정렬

        int[] problemNums = problemAlgorithms.stream()
                .mapToInt(ProblemAlgorithm::getProblemNum)
                .toArray();

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

    private List<Problem> getUserProblems(UserGroup userGroup){
        return problemRepository.selectUserProblems(userGroup.getUser().getUserId());
    }


}
