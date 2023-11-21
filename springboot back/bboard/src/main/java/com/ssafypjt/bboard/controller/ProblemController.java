package com.ssafypjt.bboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafypjt.bboard.model.dto.Group;
import com.ssafypjt.bboard.model.dto.Problem;
import com.ssafypjt.bboard.model.dto.User;
import com.ssafypjt.bboard.model.dto.UserTier;
import com.ssafypjt.bboard.model.service.GroupService;
import com.ssafypjt.bboard.model.service.ProblemService;
import com.ssafypjt.bboard.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/problem")
public class ProblemController {

    private final UserService userService;
    private final ProblemService problemService;
    private final ObjectMapper mapper;
    private final GroupService groupService;


    @Autowired
    public ProblemController(UserService userService, ProblemService problemService, ObjectMapper mapper, GroupService groupService){
        this.userService = userService;
        this.problemService = problemService;
        this.mapper = mapper;
        this.groupService = groupService;
    }

    @GetMapping("")
    public ResponseEntity<?> getProblems(){
        List<Problem> problemList = problemService.getAllProblems();
        if (problemList == null) return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<List<Problem>>(problemList, HttpStatus.OK);
    }

    @GetMapping("/tier")
    public ResponseEntity<?> getTiers(){
        List<UserTier> userTierList = problemService.getAllUserTiers();
        if (userTierList == null) return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<List<UserTier>>(userTierList, HttpStatus.OK);
    }

    @GetMapping("/tier-problem/{userId}")
    public ResponseEntity<?> getTierProblems(@PathVariable int userId){

        List<Problem> problemList = problemService.getUserTierProblems(userId);
        if (problemList == null) return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<List<Problem>>(problemList, HttpStatus.OK);
    }

    // 유저 id, group id, problem num 필요
    @PostMapping("/recomproblem")
    public ResponseEntity<?> addRecomProblem(@RequestBody Map<String, Object> requestMap){
        Problem problem = problemService.getProblemByNum(mapper.convertValue(requestMap.get("problemNum"), Integer.class));
        problem.setUserId(mapper.convertValue(requestMap.get("user"), Integer.class)); // 이렇게 해도 되나..?
        Group group = groupService.getGroup(mapper.convertValue(requestMap.get("group"), Integer.class));

        int result = problemService.addRecomProblem(problem, group.getId());
        if (result == 0)
            return new ResponseEntity<Integer>(result,HttpStatus.NO_CONTENT);
        return new ResponseEntity<Integer>(result, HttpStatus.OK);
    }


}
