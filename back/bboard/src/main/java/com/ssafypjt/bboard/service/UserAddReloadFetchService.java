package com.ssafypjt.bboard.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAddReloadFetchService {

    private final UserAddReloadService userAddReloadService;
    private final UserRepository userRepository;
    private final ProblemDomain problemDomain;
    private final UserDomain userDomain;
    private final FetchDataDomain fetchDataDomain;
    private final UserTierDomain userTierDomain;
    private final UserTierProblemDomain userTierProblemDomain;

    @Transactional
    public User userAddTask(String userName) {
        //유저 추가
        Map<String, User> map = new HashMap<>();
        var mono = Mono.defer(() ->
                fetchDataDomain.fetchOneQueryDataMono(
                                SolvedAcApi.USER.getPath(),
                                SolvedAcApi.USER.getQuery(userName)
                        )
                        .doOnNext(userNodeData -> {
                            map.put("user", userDomain.makeUserObject(userNodeData));
                        })
        );

        mono.subscribe(
                null,
                e -> log.error("error message : {}", e.getMessage()),
                () -> {
                    User user = map.get("user");
                    if (user != null) {
                        userRepository.insertUser(user);
                    }
                }
        );

        return userRepository.selectUserByName(userName);
    }


    @Async
    public void userAddUpdateTask(User user) {
        processProblem(user);
    }

    private void processProblem(User user) {
        List<ProblemAlgorithmVo> problemAlgorithmVos = new ArrayList<>();
        var mono = Mono.defer(() ->
                fetchDataDomain.fetchOneQueryDataMono(
                                SolvedAcApi.PROBLEMANDALGO.getPath(),
                                SolvedAcApi.PROBLEMANDALGO.getQuery(user.getUserName())
                        )
                        .doOnNext(problemAlgorithmData ->
                                problemAlgorithmVos.addAll(problemDomain.makeProblemAndAlgoDomainObjectMono(problemAlgorithmData, user))
                        ));

        mono.subscribe(
                null,
                e -> log.error("error message : {}", e.getMessage()),
                () -> {
                    userAddReloadService.resetProblems(problemAlgorithmVos);
                    processUserTier(user);
                }
        );
    }



    private void processUserTier(User user) {
        // 유저 : 유저 티어
        Map<Integer, List<UserTier>> totalMap = new HashMap<>();
        totalMap.put(user.getUserId(), new ArrayList<>());

        var mono = Flux.defer(() ->
                fetchDataDomain.fetchOneQueryDataUserTier(
                                SolvedAcApi.TIER.getPath(),
                                SolvedAcApi.TIER.getQuery(user.getUserName())
                        )
                        .doOnNext(userTier -> {
                                    userTier.setUserId(user.getUserId());
                                }
                        )
        );

        mono.subscribe(
                userTier -> {
                    totalMap.get(userTier.getUserId()).add(userTier);
                },
                e -> log.error("error message : {}", e.getMessage()),
                () -> {
                    userTierDomain.makeUserTierObject(totalMap.get(user.getUserId()));
                    userAddReloadService.resetUserTier(totalMap.get(user.getUserId()));
                    log.info("{} user tier changed : {}", user, totalMap.get(user.getUserId()));
                    processUserTierProblem(user, totalMap);
                }
        );

    }


    private void processUserTierProblem(User user, Map<Integer, List<UserTier>> totalMap) {
        Long cur = System.currentTimeMillis();
        List<UserPageNo> userPageNoList = userTierProblemDomain.makeUserPageNoObjectDomainList(user, totalMap.get(user.getUserId()));
        Map<User, Map<Integer, List<ProblemAlgorithmVo>>> memoMap = new HashMap<>();

        Flux.fromIterable(userPageNoList)
                .delayElements(Duration.ofMillis(1))
                .flatMap(userPageNo ->
                        fetchDataDomain.fetchOneQueryData(
                                        SolvedAcApi.USERTIERPROBLEM.getPath(),
                                        SolvedAcApi.USERTIERPROBLEM.getQuery(userPageNo.getUser().getUserName(), userPageNo.getPageNo())
                                )
                                .doOnNext(problemAlgorithmDataJsonNode -> {
                                            int pageNo = userPageNo.getPageNo();
                                            List<ProblemAlgorithmVo> list = problemDomain.makeProblemAndAlgoDomainList(problemAlgorithmDataJsonNode.path("items"), userPageNo.getUser());
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
                            userAddReloadService.resetUserTierProblems(totalProblemAndAlgoList);
                            log.info("{} user tier problem added : {}", user, totalProblemAndAlgoList.size());
                            log.info("{} user reload time : {}s", user, System.currentTimeMillis() - cur);
                        } // 완료 처리
                );
    }



}
