package com.ssafypjt.bboard.model.domain;

import com.ssafypjt.bboard.model.domain.solvedacAPI.*;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.entity.UserTier;
import com.ssafypjt.bboard.model.enums.SACApiEnum;
import com.ssafypjt.bboard.model.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

@Component
@Slf4j
public class UserAddReloadDomain {

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
    public UserAddReloadDomain(ProblemRepository problemRepository, UserRepository userRepository, ProblemAlgorithmRepository problemAlgorithmRepository, ProblemDomain problemDomain, UserDomain userDomain, FetchDomain fetchDomain, UserTierDomain userTierDomain, UserTierProblemRepository userTierProblemRepository, TierProblemRepository tierProblemRepository, UserTierProblemDomain userTierProblemDomain) {
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

        public void userAddTask(String userName) {
            processUser(userName);
        }

        @Transactional
        private void processUser(String userName) {
            Map<String, User> map = new HashMap<>();
            var mono = Mono.defer(() ->
                    fetchDomain.fetchOneQueryDataMono(
                                    SACApiEnum.USER.getPath(),
                                    SACApiEnum.USER.getQuery(userName)
                            )
                            .doOnNext(data -> {
                                map.put("user", userDomain.makeUserObject(data));
                            })
            );

            mono.subscribe(
                    null,
                    e -> log.error("error message : {}", e.getMessage()),
                    () -> {
                        User user = map.get("user");
                        if (user != null) {
                            insertUser(user);
                            User newUser = userRepository.selectUserByName(userName);
                            log.info("{} user added : ", user);
                            processProblem(newUser);
                            processUserTier(newUser);
                        }
                    }
            );

        }

        private void insertUser(User user) {
            userRepository.insertUser(user);
        }

        private void processProblem(User user) {
            List<ProblemAlgorithm> list = new ArrayList<>();
            var mono = Mono.defer(() ->
                    fetchDomain.fetchOneQueryDataMono(
                                    SACApiEnum.PROBLEMANDALGO.getPath(),
                                    SACApiEnum.PROBLEMANDALGO.getQuery(user.getUserName())
                            )
                            .doOnNext(data ->
                                    list.addAll(problemDomain.makeProblemAndAlgoDomainObjectMono(data, user))
                            ));

            mono.subscribe(
                    null, // onNext 처리는 필요 없음
                    e -> log.error("error message : {}", e.getMessage()),
                    () -> {
                        resetProblems(list);
                    } // 완료 처리
            );
        }

        private void resetProblems(List<ProblemAlgorithm> list) {
            Collections.sort(list);
            problemAlgorithmRepository.insertAlgorithms(list);
            problemRepository.insertProblems(list);

            log.info("problems added : {}", list.size());
            log.info("add problems are {}", list.size());
        }

        private void processUserTier(User user) {
            Map<Integer, List<UserTier>> totalMap = new HashMap<>();
            totalMap.put(user.getUserId(), new ArrayList<>());

            var mono = Flux.defer(() ->
                    fetchDomain.fetchOneQueryDataUserTier(
                                    SACApiEnum.TIER.getPath(),
                                    SACApiEnum.TIER.getQuery(user.getUserName())
                            )
                            .doOnNext(data -> {
                                        data.setUserId(user.getUserId());
                                    }
                            )
            );

            mono.subscribe(
                    data -> {
                        totalMap.get(data.getUserId()).add(data);
                    },
                    e -> log.error("error message : {}", e.getMessage()),
                    () -> {
                        userTierDomain.makeUserTierObject(totalMap.get(user.getUserId()));
                        resetUserTier(totalMap.get(user.getUserId()));
                        log.info("{} user tier changed : {}", user, totalMap.get(user.getUserId()));
                        processUserTierProblem(user, totalMap);
                    }
            );

        }

        private void resetUserTier(List<UserTier> list) {
            tierProblemRepository.insertUserTiers(list);
        }

        private void processUserTierProblem(User user, Map<Integer, List<UserTier>> totalMap) {
            Long cur = System.currentTimeMillis();
            List<UserPageNo> userPageNoList = userTierProblemDomain.makeUserPageNoObjectDomainList(user, totalMap.get(user.getUserId()));
            Map<User, Map<Integer, List<ProblemAlgorithm>>> memoMap = new HashMap<>();

            Flux.fromIterable(userPageNoList)
                    .delayElements(Duration.ofMillis(1))
                    .flatMap(userPageNo ->
                            fetchDomain.fetchOneQueryData(
                                            SACApiEnum.USERTIERPROBLEM.getPath(),
                                            SACApiEnum.USERTIERPROBLEM.getQuery(userPageNo.getUser().getUserName(), userPageNo.getPageNo())
                                    )
                                    .doOnNext(data -> {
                                                int pageNo = userPageNo.getPageNo();
                                                List<ProblemAlgorithm> list = problemDomain.makeProblemAndAlgoDomainList(data.path("items"), userPageNo.getUser());
                                                synchronized (memoMap) {
                                                    memoMap.putIfAbsent(user, new HashMap<>());
                                                    memoMap.get(user).putIfAbsent(pageNo, list);
                                                }
                                            }
                                    )

                    )
                    .subscribe(
                            null, // onNext 처리는 필요 없음
                            e -> log.error("error message : {}", e.getMessage()),
                            () -> {
                                List<ProblemAlgorithm> totalProblemAndAlgoList = userTierProblemDomain.makeTotalProblemAndAlgoList(memoMap, totalMap);
                                resetUserTierProblems(totalProblemAndAlgoList);
                                log.info("{} user tier problem added : {}", user, totalProblemAndAlgoList.size());
                                log.info("{} user reload time : {}s", user, System.currentTimeMillis() - cur);
                            } // 완료 처리
                    );
        }

        private void resetUserTierProblems(List<ProblemAlgorithm> list) {
            problemAlgorithmRepository.insertAlgorithms(list);
            userTierProblemRepository.insertTierProblems(list);
        }

}
