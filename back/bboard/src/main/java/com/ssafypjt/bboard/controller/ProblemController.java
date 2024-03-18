package com.ssafypjt.bboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafypjt.bboard.model.entity.Problem;
import com.ssafypjt.bboard.model.entity.UserTier;
import com.ssafypjt.bboard.model.service.ProblemService;
import com.ssafypjt.bboard.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/problem")
@Slf4j
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final ObjectMapper mapper;
    private final SessionManager sessionManager;

    @GetMapping("")
    public ResponseEntity<?> getProblems(){
        List<Problem> problemList = problemService.getAllProblems();
        if (problemList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(problemList, HttpStatus.OK);
    }


    @GetMapping("/tier")
    public ResponseEntity<?> getTiers(){
        List<UserTier> userTierList = problemService.getAllUserTiers();
        if (userTierList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(userTierList, HttpStatus.OK);
    }

    @GetMapping("/tier-problem/{userId}")
    public ResponseEntity<?> getTierProblems(@PathVariable int userId){
        List<Problem> problemList = problemService.getUserTierProblems(userId);
        if (problemList == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(problemList, HttpStatus.OK);
    }

    @PostMapping("/recomproblem")
    public ResponseEntity<?> addRecomProblem(@RequestBody Map<String, Object> requestMap, HttpServletRequest request){
        int problemId = (mapper.convertValue(requestMap.get("problemNum"), Integer.class));
        int groupId = (mapper.convertValue(requestMap.get("group"), Integer.class));
        int userId = (Integer) sessionManager.getSession(request);

        int result = problemService.addRecomProblem(problemId, groupId, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
