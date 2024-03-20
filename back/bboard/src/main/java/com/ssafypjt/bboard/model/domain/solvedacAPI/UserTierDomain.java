package com.ssafypjt.bboard.model.domain.solvedacAPI;

import com.ssafypjt.bboard.model.entity.UserTier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTierDomain {

    final int MAX_TIER = 30;
    final int NUMBER_OF_PAGES = 50;

    public List<UserTier> makeUserTierObject(List<UserTier> userTierList){
        // User 1개 당
        // 0 ~ 30 까지 총 31개의 Tier가 존재 가능
        int[] problemPrefixSum = new int[MAX_TIER+2];
        // 해당 티어 문제의 위치 (몇번째 페이지 / 몇번째 문제) 계산
        for (int i = MAX_TIER; i >= 0; i--) {
            UserTier userTier = userTierList.get(i);
            problemPrefixSum[i] = userTier.getProblemCount() +  problemPrefixSum[i+1];
            if (problemPrefixSum[i+1] == 0) continue;

            int pageNo = problemPrefixSum[i+1] / NUMBER_OF_PAGES + 1;
            int pageIdx = problemPrefixSum[i+1] % NUMBER_OF_PAGES == 0 ? 0 : problemPrefixSum[i+1] % NUMBER_OF_PAGES;
            userTier.setPageNo(pageNo);
            userTier.setPageIdx(pageIdx);
        }
        return userTierList;
    }

}
