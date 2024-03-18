package com.ssafypjt.bboard.model.domain;

import com.ssafypjt.bboard.model.domain.solvedacAPI.*;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.entity.UserTier;
import com.ssafypjt.bboard.model.enums.SolvedAcApi;
import com.ssafypjt.bboard.model.repository.*;
import com.ssafypjt.bboard.model.vo.ProblemAlgorithmVo;
import com.ssafypjt.bboard.model.vo.UserPageNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

@Component
@Slf4j
public class ReloadDomain {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final ProblemAlgorithmRepository problemAlgorithmRepository;
    private final ProblemDomain problemDomain;
    private final UserDomain userDomain;
    private final FetchDomain fetchDomain;
    private final UserTierDomain userTierDomain;
    private final UserTierProblemRepository userTierProblemRepository;
    private final TierProblemRepository tierProblemRepository;
    private final UserTierProblemDomain userTierProblemDomain;

    @Autowired
    public ReloadDomain(ProblemRepository problemRepository, UserRepository userRepository, ProblemAlgorithmRepository problemAlgorithmRepository, ProblemDomain problemDomain, UserDomain userDomain, FetchDomain fetchDomain, UserTierDomain userTierDomain, UserTierProblemRepository userTierProblemRepository, TierProblemRepository tierProblemRepository, UserTierProblemDomain userTierProblemDomain) {
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.problemAlgorithmRepository = problemAlgorithmRepository;
        this.problemDomain = problemDomain;
        this.userDomain = userDomain;
        this.fetchDomain = fetchDomain;
        this.userTierDomain = userTierDomain;
        this.userTierProblemRepository = userTierProblemRepository;
        this.tierProblemRepository = tierProblemRepository;
        this.userTierProblemDomain = userTierProblemDomain;
    }

    @Scheduled(fixedRate = 600000)
    @Transactional
    @Async
    public void scheduledTask() {
        int maxThreads = Runtime.getRuntime().availableProcessors();
        log.info("maxThreads : {}", maxThreads);
        log.info("현재 scheduledTask 스레드 이름: {}", Thread.currentThread().getName());
        //유저 정보 업데이트
        List<User> users = userRepository.selectAllUser();
        processUser(users);
    }


    // 유저 정보 업데이트
    @Async("taskExecutor")
    public void processUser(List<User> users) {
        Long cur = System.currentTimeMillis();
        List<User> userList = userDomain.getUserList();
            userList.clear();
            Flux.fromIterable(users)
                    .delayElements(Duration.ofMillis(1))
                    .flatMap(user ->
                            fetchDomain.fetchOneQueryData(
                                            SolvedAcApi.USER.getPath(),
                                            SolvedAcApi.USER.getQuery(user.getUserName())
                                    )
                                    .doOnNext(data -> {
                                        log.info("현재 processUser 스레드 이름: {}", Thread.currentThread().getName());
                                        User newUser = userDomain.makeUserObject(data);
                                                updateUser(newUser);
                                            }
                                    )
                    )
                    .subscribe(
                            null, // onNext 처리는 필요 없음
                            e -> log.error("error message : {}", e.getMessage()),
                            () -> {
                                log.info("updated user : {}", users.size());
                                log.info("reset time : {} ms", System.currentTimeMillis() - cur);

                                //유저 목록을 사용한 상위 문제 100개 가져오기
                                List<User> newUsers = userRepository.selectAllUser();
                                processProblem(newUsers);
                            }
                    );
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    // 문제 리셋
    public void processProblem(List<User> users) {
        Long cur = System.currentTimeMillis();
        List<ProblemAlgorithmVo> problemAlgorithmVos = new ArrayList<>();
            Flux.fromIterable(users)
                    .delayElements(Duration.ofMillis(1))
                    .flatMap(user ->
                            fetchDomain.fetchOneQueryData(
                                            SolvedAcApi.PROBLEMANDALGO.getPath(),
                                            SolvedAcApi.PROBLEMANDALGO.getQuery(user.getUserName())
                                    )
                                    .doOnNext(data -> {
                                        log.info("현재 processProblem 스레드 이름: {}", Thread.currentThread().getName());
                                        problemDomain.makeProblemAndAlgoDomainObject(problemAlgorithmVos, data, user);
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

                                //유저의 티어별 문제 갯수 받아오기
                                processUserTier(users);
                            } // 완료 처리
                    );

    }

    public void resetProblems(List<ProblemAlgorithmVo> list) {
        // 기존 테이블 삭제
        problemRepository.deleteAll();
        userTierProblemRepository.deleteAll(); // 티어별 문제도 삭제해야 알고리즘 삭제 가능
        problemAlgorithmRepository.deleteAll();
        Collections.sort(list);
        problemAlgorithmRepository.insertAlgorithms(list);
        problemRepository.insertProblems(list);
    }


    public void processUserTier(List<User> users) {
        Long cur = System.currentTimeMillis();
        Map<Integer, List<UserTier>> totalMap = new HashMap<>();
        for (User user : users) {
            totalMap.put(user.getUserId(), new ArrayList<>());
        }
            Flux.fromIterable(users)
                    .delayElements(Duration.ofMillis(1))
                    .flatMap(user ->
                            fetchDomain.fetchOneQueryDataUserTier(
                                            SolvedAcApi.TIER.getPath(),
                                            SolvedAcApi.TIER.getQuery(user.getUserName())
                                    )
                                    .doOnNext(data -> {
                                        log.info("현재 processUserTier 스레드 이름: {}", Thread.currentThread().getName());
                                        data.setUserId(user.getUserId());
                                            }
                                    )
                    )
                    .subscribe(
                            data -> {
                                totalMap.get(data.getUserId()).add(data);
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
                            } // 완료 처리
                    );
    }

    public void resetUserTier(Map<Integer, List<UserTier>> totalMap) {
        tierProblemRepository.deleteAll();
        for (List<UserTier> userTierList : totalMap.values()) {
            tierProblemRepository.insertUserTiers(userTierList);
        }
    }

    // problemDomain 코드 재시용
    public void processUserTierProblem(List<User> users, Map<Integer, List<UserTier>> totalMap) {
        Long cur = System.currentTimeMillis();
        List<UserPageNo> userPageNoList = userTierProblemDomain.makeUserPageNoObjectDomainList(users, totalMap);
        Map<User, Map<Integer, List<ProblemAlgorithmVo>>> memoMap = new HashMap<>();
        log.info("userPageNoList size : {}", userPageNoList.size());
        Flux.fromIterable(userPageNoList)
                .delayElements(Duration.ofMillis(1))
                .flatMap(userPageNo ->
                    fetchDomain.fetchOneQueryData(
                                    SolvedAcApi.USERTIERPROBLEM.getPath(),
                                    SolvedAcApi.USERTIERPROBLEM.getQuery(userPageNo.getUser().getUserName(), userPageNo.getPageNo())
                            )
                            .doOnNext(data -> {
                                log.info("현재 processUserTierProblem 스레드 이름: {}", Thread.currentThread().getName());
                                User user = userPageNo.getUser();
                                        int pageNo = userPageNo.getPageNo();
                                        List<ProblemAlgorithmVo> list = problemDomain.makeProblemAndAlgoDomainList(data.path("items"), userPageNo.getUser());
                                        memoMap.putIfAbsent(user, new HashMap<>());
                                        memoMap.get(user).putIfAbsent(pageNo, list);
                                    }
                            )
                )
                .subscribe(
                        null, // onNext 처리는 필요 없음
                        e -> log.error("error message : {}", e.getMessage()),
                        () -> {
                            List<ProblemAlgorithmVo> totalProblemAndAlgoList = userTierProblemDomain.makeTotalProblemAndAlgoList(memoMap, totalMap);
                            resetUserTierProblems(totalProblemAndAlgoList);
                            log.info("updated user-tier-problem : {}", totalProblemAndAlgoList.size());
                            log.info("reset time : {} ms", System.currentTimeMillis() - cur);
                        } // 완료 처리
                );
    }


    public void resetUserTierProblems(List<ProblemAlgorithmVo> list) {
        userTierProblemRepository.deleteAll();
        problemAlgorithmRepository.insertAlgorithms(list);
        userTierProblemRepository.insertTierProblems(list);
    }

}
