package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.entity.UserTier;
import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import com.ssafypjt.bboard.model.vo.UserPageNo;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@NoArgsConstructor
public class UserTierProblemDomain {

    public List<UserPageNo> makeUserPageNoObjectDomainList (List<User> users, Map<Integer, List<UserTier>> totalMap){
        Set<String> set = new HashSet<>();
        List<UserPageNo> userPageNoList = new ArrayList<>();
        for(User user : users){
            for(UserTier userTier : totalMap.get(user.getUserId())){
                if (set.add(userTier.getUserId() + " " + userTier.getPageNo())){
                    userPageNoList.add(new UserPageNo(user, userTier.getPageNo()));
                }
            }
        }
        return userPageNoList;
    }

    public List<UserPageNo> makeUserPageNoObjectDomainList(User user, List<UserTier> userTiers){
        Set<String> set = new HashSet<>();
        List<UserPageNo> userPageNoList = new ArrayList<>();
            for(UserTier userTier : userTiers) {
                if (set.add(userTier.getUserId() + " " + userTier.getPageNo())) {
                    userPageNoList.add(new UserPageNo(user, userTier.getPageNo()));
                }
            }
        return userPageNoList;
    }

    // 전체 유저에 대한 problem & algorithm 리스트 생성
    public List<ProblemAlgorithmVo> makeTotalProblemAndAlgoList(Map<User, Map<Integer,
            List<ProblemAlgorithmVo>>> memoMap, Map<Integer, List<UserTier>> userTierMap){

        List<ProblemAlgorithmVo> totalProblemAndAlgoList = new ArrayList<>();

        // 유저당 Problem & Alogorithm VO 생성
        for (User user: memoMap.keySet()) {
            List<UserTier> userTierList = userTierMap.get(user.getUserId()); // 유저당 userTier 데이터 저장된 맵
            int prevPage = 0;
            List<ProblemAlgorithmVo> problemListByPage = null;
            for (UserTier userTier : userTierList) {

                // 메모이제이션 활용
                // 직전에 API로 가져온 페이지가 아닐 때만 추가로 API 요청하도록 memoMap에 기록
                if(prevPage != userTier.getPageNo()){
                    problemListByPage = memoMap.get(user).get(userTier.getPageNo());
                    prevPage = userTier.getPageNo();
                }

                if (userTier.getProblemCount() != 0) {
                    assert problemListByPage != null;

                    ProblemAlgorithmVo problemAndAlgoVo = problemListByPage.get(userTier.getPageIdx());
                    problemAndAlgoVo.getProblem().setUserId(user.getUserId());
                    totalProblemAndAlgoList.add(problemAndAlgoVo);
                }
            }
        }

        return totalProblemAndAlgoList;
    }



}
