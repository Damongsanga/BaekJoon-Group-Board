package com.ssafypjt.bboard.tierProblemTest;

import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.UserTier;
import com.ssafypjt.bboard.model.repository.UserTierProblemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TierProblemAPITest {

    @Autowired
    UserTierProblemRepository userTierProblemRepository;

    @Test
    public void TEST(){

        // 아래 모든 로직은 유저별로 반복적으로 돌려야함
        // 아래 코드들은 1명의 유저들을 대상으로 user_tier_problem을 구하는 코드

        final int MAX_TIER = 30;
        final int NUMBER_OF_PAGES = 50;

        // test용 UserTier 만들기
        // 실제로는 api로 받아오면 됨
        List<UserTier> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j <= MAX_TIER; j++) {
                UserTier tier = new UserTier();
                tier.setUserId(i);
                tier.setTier(j);
                tier.setProblemCount((int) (Math.random() * 100));
                list.add(tier);
            }
        }
        list.get(30).setProblemCount(0);
        list.get(29).setProblemCount(0);
        list.get(28).setProblemCount(0);

        for (UserTier now : list){
            System.out.println(now);
        }

        // User 1개 당
        // 0 ~ 30 까지 총 31개의 Tier가 존재 가능한 것으로 보임
        int[] problemPrefixSum = new int[MAX_TIER+2];
        for (int i = MAX_TIER; i >= 0; i--) {
            UserTier now = list.get(i);
            problemPrefixSum[i] = now.getProblemCount() +  problemPrefixSum[i+1];
             if (problemPrefixSum[i+1] == 0) continue;
            now.setPageNo(problemPrefixSum[i+1] % NUMBER_OF_PAGES == 0 ? problemPrefixSum[i+1] / NUMBER_OF_PAGES + 2 : problemPrefixSum[i+1] / NUMBER_OF_PAGES + 1);
            now.setPageIdx(problemPrefixSum[i+1] % NUMBER_OF_PAGES == 0 ? NUMBER_OF_PAGES : problemPrefixSum[i+1] % NUMBER_OF_PAGES);
        }

        System.out.println(Arrays.toString(problemPrefixSum));
        for (UserTier now : list){
            System.out.println(now);
        }

        // UserTierProblem 생성하여 user_tier_problem 테이블에 넣는 로직
        userTierProblemRepository.deleteAll();
        int currPage = 0;
        // 맵으로 수정
        List<ProblemAlgorithmVo> problemAlgorithmVoList = null;
        for (int i = MAX_TIER; i >=0; i--) {
            UserTier now = list.get(i);
            if (now.getPageNo() != currPage){ // 페이지를 안가져온 경우에만 API 호출
                currPage = now.getPageNo();
                // problemAndAlgoObjectDomainList = 페이지를 가져오는 API
            }
            // problemList.get(now.getPageIdx()) 로 문제를 가져올 것 (맨 앞 문제임, random 로직 추가 구현 가능)
            //
            Problem problem = new Problem(); // 선택된 problem 예시
            userTierProblemRepository.insertTierProblem(problem);
        }


    }
}
