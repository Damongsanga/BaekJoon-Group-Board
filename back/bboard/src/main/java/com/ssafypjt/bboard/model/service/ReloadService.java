package com.ssafypjt.bboard.model.service;

import com.ssafypjt.bboard.model.domain.solvedacAPI.*;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.entity.UserTier;
import com.ssafypjt.bboard.model.enums.SolvedAcApi;
import com.ssafypjt.bboard.model.repository.*;
import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import com.ssafypjt.bboard.model.vo.UserPageNo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReloadService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final ProblemAlgorithmRepository problemAlgorithmRepository;
    private final ProblemDomain problemDomain;
    private final UserDomain userDomain;
    private final FetchDataDomain fetchDataDomain;
    private final UserTierDomain userTierDomain;
    private final UserTierProblemRepository userTierProblemRepository;
    private final TierProblemRepository tierProblemRepository;
    private final UserTierProblemDomain userTierProblemDomain;

    @Scheduled(fixedRate = 600000)
    @Transactional
    @Async
    public void scheduledTask() {
        log.info("maxThreads : {}", Runtime.getRuntime().availableProcessors());
        //유저 정보 업데이트
        processUser(userRepository.selectAllUsers());
    }


    // 유저 정보 업데이트
    private void processUser(List<User> users) {
        Long cur = System.currentTimeMillis();
        Flux.fromIterable(users)
                .delayElements(Duration.ofMillis(1))
                .flatMap(user ->
                        fetchDataDomain.fetchOneQueryData(
                                        SolvedAcApi.USER.getPath(),
                                        SolvedAcApi.USER.getQuery(user.getUserName())
                                )
                                .doOnNext(userNodeData -> {
                                            updateUser(userDomain.makeUserObject(userNodeData));
                                        }
                                )
                )
                .subscribe(
                        null,
                        e -> log.error("error message : {}", e.getMessage()),
                        () -> {
                            log.info("updated user : {}", users.size());
                            log.info("reset time : {} ms", System.currentTimeMillis() - cur);
                            //유저 목록을 사용한 상위 문제 100개 가져오기
                            processProblem(userRepository.selectAllUsers());
                        }
                );
    }

    private void updateUser(User user) {
        userRepository.updateUser(user);
    }

    // 문제 리셋
    private void processProblem(List<User> users) {
        Long cur = System.currentTimeMillis();
        List<ProblemAlgorithmVo> problemAlgorithmVos = new ArrayList<>();
        Flux.fromIterable(users)
                .delayElements(Duration.ofMillis(1))
                .flatMap(user ->
                        fetchDataDomain.fetchOneQueryData(
                                        SolvedAcApi.PROBLEMANDALGO.getPath(),
                                        SolvedAcApi.PROBLEMANDALGO.getQuery(user.getUserName())
                                )
                                .doOnNext(problemAlgorithmData -> {
                                            problemDomain.makeProblemAndAlgoDomainObject(problemAlgorithmVos, problemAlgorithmData, user);
                                        }
                                )
                ).then()
                .subscribe(
                        null, // onNext 처리는 필요 없음
                        e -> log.error("error message : {}", e.getMessage()),
                        () -> {
                            resetProblems(problemAlgorithmVos);
                            log.info("updated problem : {}", problemAlgorithmVos.size());
                            log.info("reset time : {} ms", System.currentTimeMillis() - cur);

                            // 유저의 티어별 문제 갯수 받아오기
                            processUserTier(users);
                        }
                );

    }

    private void resetProblems(List<ProblemAlgorithmVo> list) {
        // 기존 테이블 삭제
        problemRepository.deleteAll();
        userTierProblemRepository.deleteAll(); // 티어별 문제도 삭제해야 알고리즘 삭제 가능
        problemAlgorithmRepository.deleteAll();

        Collections.sort(list);
        problemAlgorithmRepository.insertAlgorithms(list); // 알고리즘 먼저 추가 필요
        problemRepository.insertProblems(list);
    }


    private void processUserTier(List<User> users) {
        Long cur = System.currentTimeMillis();

        // 유저 : 유저 티어
        Map<Integer, List<UserTier>> totalMap = new HashMap<>();
        for (User user : users) {
            totalMap.put(user.getUserId(), new ArrayList<>());
        }

        Flux.fromIterable(users)
                .delayElements(Duration.ofMillis(1))
                .flatMap(user ->
                        fetchDataDomain.fetchOneQueryDataUserTier(
                                        SolvedAcApi.TIER.getPath(),
                                        SolvedAcApi.TIER.getQuery(user.getUserName())
                                ) // 유저 티어 반환
                                .doOnNext(userTier -> {
                                            userTier.setUserId(user.getUserId());
                                        }
                                )
                )
                .subscribe(
                        userTier -> {
                            totalMap.get(userTier.getUserId()).add(userTier);
                        },
                        e -> log.error("error message : {}", e.getMessage()),
                        () -> {
                            for (Integer userId : totalMap.keySet()) {
                                List<UserTier> userTierList = totalMap.get(userId);
                                userTierDomain.makeUserTierObject(userTierList);
                            }
                            resetUserTier(totalMap);
                            log.info("tier updated user : {}", users.size());
                            log.info("reset time : {} ms", System.currentTimeMillis() - cur);
                            processUserTierProblem(users, totalMap);
                        }
                );
    }

    private void resetUserTier(Map<Integer, List<UserTier>> totalMap) {
        tierProblemRepository.deleteAll();
        for (List<UserTier> userTierList : totalMap.values()) {
            tierProblemRepository.insertUserTiers(userTierList);
        }
    }

    // problemDomain 코드 재시용
    private void processUserTierProblem(List<User> users, Map<Integer, List<UserTier>> totalMap) {
        long cur = System.currentTimeMillis();

        // 유저당 API 요청이 필요한 페이지 번호 List
        List<UserPageNo> userPageNoList = userTierProblemDomain.makeUserPageNoObjectDomainList(users, totalMap);
        // 총 API 요청 횟수
        log.info("userPageNoList size : {}", userPageNoList.size());

        // [유저] : [페이지 번호] : [문제&알고리즘 리스트]
        Map<User, Map<Integer, List<ProblemAlgorithmVo>>> memoMap = new HashMap<>();

        Flux.fromIterable(userPageNoList)
                .delayElements(Duration.ofMillis(1))
                .flatMap(userPageNo ->
                        // 현재 유저의 푼 문제 페이징 API 요청
                        fetchDataDomain.fetchOneQueryData(
                                        SolvedAcApi.USERTIERPROBLEM.getPath(),
                                        SolvedAcApi.USERTIERPROBLEM.getQuery(userPageNo.getUser().getUserName(), userPageNo.getPageNo())
                                )
                                .doOnNext(problemAlgorithmDataJsonNode -> {
                                            User user = userPageNo.getUser();
                                            int pageNo = userPageNo.getPageNo();

                                            List<ProblemAlgorithmVo> list = problemDomain.makeProblemAndAlgoDomainList(problemAlgorithmDataJsonNode.path("items"), user);

                                            memoMap.putIfAbsent(user, new HashMap<>());
                                            memoMap.get(user).putIfAbsent(pageNo, list);
                                        }
                                )
                )
                .subscribe(
                        null,
                        e -> log.error("error message : {}", e.getMessage()),
                        () -> {
                            List<ProblemAlgorithmVo> totalProblemAndAlgoList = userTierProblemDomain.makeTotalProblemAndAlgoList(memoMap, totalMap);
                            resetUserTierProblems(totalProblemAndAlgoList);
                            log.info("updated user-tier-problem : {}", totalProblemAndAlgoList.size());
                            log.info("reset time : {} ms", System.currentTimeMillis() - cur);
                        }
                );
    }

    private void resetUserTierProblems(List<ProblemAlgorithmVo> list) {
        userTierProblemRepository.deleteAll();
        problemAlgorithmRepository.insertAlgorithms(list);
        userTierProblemRepository.insertTierProblems(list);
    }

}
