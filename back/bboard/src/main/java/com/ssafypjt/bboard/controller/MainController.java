package com.ssafypjt.bboard.controller;

import com.ssafypjt.bboard.model.service.GroupDataService;
import com.ssafypjt.bboard.model.entity.UserGroup;
import com.ssafypjt.bboard.model.entity.*;
import com.ssafypjt.bboard.model.service.GroupService;
import com.ssafypjt.bboard.model.service.UserService;
import com.ssafypjt.bboard.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final GroupService groupService;
    private final GroupDataService groupDataService;
    private final SessionManager sessionManager;

    // 그룹 page에서 Main page 진입시 그룹 정보 반환
    // 만약 로그인된 유저가 해당 그룹에 등록되지 않았으면 BAD_REQUEST
    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getGroupInfo(@PathVariable int groupId, HttpServletRequest request){
        Group group = groupService.getGroup(groupId);
        User user = userService.getUser(getCurrentUserId(request));

        if (!userService.getGroupIdByUser(user.getUserId()).contains(groupId)){
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(groupDataService.getData(new UserGroup(user, group)), HttpStatus.OK);
    }

    private int getCurrentUserId(HttpServletRequest request) {
        return (Integer) sessionManager.getSession(request);
    }

}
