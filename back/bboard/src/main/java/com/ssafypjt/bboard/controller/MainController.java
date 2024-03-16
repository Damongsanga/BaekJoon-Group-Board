package com.ssafypjt.bboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafypjt.bboard.model.domain.groupinfo.GroupDomain;
import com.ssafypjt.bboard.model.domain.groupinfo.UserGroup;
import com.ssafypjt.bboard.model.entity.*;
import com.ssafypjt.bboard.model.service.GroupService;
import com.ssafypjt.bboard.model.service.ProblemService;
import com.ssafypjt.bboard.model.service.UserService;
import com.ssafypjt.bboard.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/main")
@Slf4j
public class MainController {

    private final UserService userService;
    private final ProblemService problemService;
    private final GroupService groupService;
    private final ObjectMapper mapper;
    private final GroupDomain groupDomain;
    private final SessionManager sessionManager;

    @Autowired
    public MainController(UserService userService, ProblemService problemService, GroupService groupService, ObjectMapper mapper, GroupDomain groupDomain, SessionManager sessionManager){
        this.userService = userService;
        this.problemService = problemService;
        this.groupService = groupService;
        this.mapper = mapper;
        this.groupDomain = groupDomain;
        this.sessionManager = sessionManager;
    }

    // 그룹 page에서 Main page 진입시 그룹 정보 반환
    // 만약 로그인된 유저가 해당 그룹에 등록되지 않았으면 BAD_REQUEST
    @GetMapping("/group/{groupId}")
    @Transactional
    public ResponseEntity<?> getGroupInfo(@PathVariable int groupId, HttpServletRequest request){ // ObjectNode (JSON DATA)로 전송
        Group group = groupService.getGroup(groupId);
        User user = userService.getUser((Integer) sessionManager.getSession(request));

        if (!userService.getGroupIdByUser(user.getUserId()).contains(groupId)){
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        ObjectNode responseJson = getJsonNodes(group, user);

        return new ResponseEntity<ObjectNode>(responseJson, HttpStatus.OK);
    }

    private ObjectNode getJsonNodes(Group group, User user) {
        UserGroup userAndGroup = new UserGroup(user, group);
        List<User> userList = groupDomain.getUsers(userAndGroup); // 그룹 해당 유저 정보
        List<Problem> top100problemList = groupDomain.getProblems(userAndGroup, userList); // 그룹별 top 100개 문제 정보
        List<Problem> userTierProblemList = groupDomain.getUserTierProblems(userAndGroup, userList); // 그룹별 로그인된 유저에 해당하는 userTierProblem 정보
        List<RecomProblem> recomProblemList = groupDomain.getRecomProblems(userAndGroup); // 그룹별 recomProblem 정보
        List<ProblemAlgorithm> algorithmList = groupDomain.getAlgorithms(top100problemList, recomProblemList); // 그룹의 모든 문제의 알고리즘 정보
        List<Problem> userTop100ProblemList = groupDomain.getUserProblems(userAndGroup); // 유저의 top 100개 문제 정보 (Recomproblem 등록 위해)

        ObjectNode responseJson = JsonNodeFactory.instance.objectNode();
        responseJson.set("user", mapper.valueToTree(user));
        responseJson.set("group", mapper.valueToTree(group));
        responseJson.set("users", mapper.valueToTree(userList));
        responseJson.set("top100problems", mapper.valueToTree(top100problemList));
        responseJson.set("userTierProblems", mapper.valueToTree((userTierProblemList)));
        responseJson.set("recomProblems", mapper.valueToTree(recomProblemList));
        responseJson.set("algorithms", mapper.valueToTree(algorithmList));
        responseJson.set("userTop100problems", mapper.valueToTree(userTop100ProblemList));
        return responseJson;
    }


}
