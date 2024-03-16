package com.ssafypjt.bboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafypjt.bboard.model.domain.UserAddReloadDomain;
import com.ssafypjt.bboard.model.entity.Group;
import com.ssafypjt.bboard.model.entity.User;
import com.ssafypjt.bboard.model.service.GroupService;
import com.ssafypjt.bboard.model.service.UserService;
import com.ssafypjt.bboard.session.SessionConst;
import com.ssafypjt.bboard.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/user-group")
public class UserGroupController {

    private UserService userService;
    private GroupService groupService;
    private ObjectMapper mapper;
    private UserAddReloadDomain userAddReloadDomain;
    private SessionManager sessionManager;

    @Autowired
    private UserGroupController(UserService userService, GroupService groupService, ObjectMapper mapper, UserAddReloadDomain userAddReloadDomain, SessionManager sessionManager) {
        this.userService = userService;
        this.groupService = groupService;
        this.mapper = mapper;
        this.userAddReloadDomain = userAddReloadDomain;
        this.sessionManager = sessionManager;
    }

    // 유저 입력 페이지

    // 로그인
    @GetMapping("login/{userName}")
    public ResponseEntity<?> login(@PathVariable String userName,
                                   @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Integer userId,
                                   HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        User user = userService.getUserByName(userName);
        if (user == null) return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        sessionManager.createSession(user.getUserId(), response);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        sessionManager.expire(request);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    // 유저 id 입력하여 유저 반환
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable int userId){
        User user = userService.getUser(userId);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    // 모든 유저정보 반환
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> userList = userService.getAllUser();
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    // 유저 등록
    // 리턴 작동 안함
    // 유저가 이미 등록되면 IM_USED
    // 이미 30명이 넘었으면 BAD_REQUEST
    // 이외 (정상 등록 & 백준에 없는 아이디 입력) OK
    // 문제 : 백준에 없는 아이디를 입력했을 때와 정상적으로 등록되었을 때 구분 불가능
    // 비동기로 처리되기 때문에 리턴값이 없도록 구현..
    @GetMapping("/user/add/{userName}")
    public ResponseEntity<Void> regist(@PathVariable String userName){

        if (userService.getUserByName(userName) != null)
            return new ResponseEntity<Void>(HttpStatus.IM_USED);
        if (userService.getAllUser().size() >= 30)
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

            userAddReloadDomain.userAddTask(userName);
            User user = userService.getUserByName(userName);
            return new ResponseEntity<Void>(HttpStatus.OK);
    }



    // 그룹 선택 페이지
    // 유저가 속한 GROUP 정보 반환
    // 그룹이 없으면 NO_CONTENT
    @GetMapping("/group") // 로그인된 유저의 그룹 정보 가져오기
    public ResponseEntity<?> getGroups(HttpServletRequest request){
        int userId = (Integer) sessionManager.getSession(request);
        List<Group> list = groupService.getGroups(userId);
        list.forEach(group -> group.setPassword(group.getPassword().replaceAll(".", "*")));

        if (list == null) return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<List<Group>>(list, HttpStatus.OK);
    }

    @GetMapping("/user/loginuser") // 로그인된 유저의 그룹 정보 가져오기
    public ResponseEntity<User> getLoginUser(HttpServletRequest request){
        User user = userService.getUser((Integer) sessionManager.getSession(request));
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    // 그룹 등록
    // 그룹 정상 생성시 group 반환 (비밀번호는 암호화되어서 리턴)
    // 비밀번호 4자리 이하이면 BAD_REQUEST
    // 그룹명 중복이면 IM_USED
    // 해당 유저에가 이미 3개의 그룹에 가입되어있으면 FORBIDDEN
    @PostMapping("/group/add")
    public ResponseEntity<?> addGroup(@RequestBody Group group, HttpServletRequest request){
        User user = userService.getUser((Integer) sessionManager.getSession(request));

        if (group.getPassword().length() < 4) return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

        int result = groupService.makeGroup(user, group);
        if (result == 0) return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        if (result == -1) return new ResponseEntity<Void>(HttpStatus.IM_USED);

        groupService.addUser(group, user.getUserId());
        group = groupService.getGroupByName(group.getGroupName());
        group.setPassword(group.getPassword().replaceAll(".", "*"));

        return new ResponseEntity<Group>(group, HttpStatus.OK);
    }

    // 그룹 탈퇴
    // 그룹 탈퇴 성공시 OK, 1
    // 오류 발생시 BAD_REQUEST
    // 그룹 탈퇴로 인해 그룹 삭제시 OK, -1 리턴;
    @GetMapping("/group/leave/{groupId}") // 그룹 탈퇴
    public ResponseEntity<?> leaveGroup(@PathVariable int groupId, HttpServletRequest request){
        Group group = groupService.getGroup(groupId);
        User user = userService.getUser((Integer) sessionManager.getSession(request));
        int result = groupService.removeUser(group.getId(), user.getUserId());
        if(result == 0) new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);

        // 만약 탈퇴한 유저가 마지막 유저였다면 그룹을 삭제
        if (groupService.getUsers(group.getId()).size() == 0){
            groupService.removeGroup(group.getId());
            result = -1;
        }

        return new ResponseEntity<Integer>(result, HttpStatus.OK);
    }


    // 관리자 페이지 기능

    // 관리자 권한 확인 (비밀번호로 확인)
    // login 여부 boolean 반환
    @PostMapping("/group/admin")
    public ResponseEntity<Boolean> adminLogin(@RequestBody Map<String, Object> requestMap){
        Group group = groupService.getGroup(mapper.convertValue(requestMap.get("group"), Integer.class));
        String inputPassword = mapper.convertValue(requestMap.get("password"), String.class);
        if (groupService.adminValid(group.getId(), inputPassword)){
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.OK);
    }

    // 그룹 삭제
    // 정상 작동시 삭제된 그룹의 그룹 id 반환
    // 유저 삭제 비정상 작동시 BAD_REQUEST
    @DeleteMapping("/group/admin/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable int groupId){
        int result = groupService.removeGroup(groupId);
        if (result == 0) return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Integer>(groupId, HttpStatus.OK);
    }

    // 관리자 페이지에서 띄워줄 그룹 유저
    @GetMapping("/group/admin/{groupId}")
    public ResponseEntity<?> getUsers(@PathVariable int groupId){
        List<User> list = groupService.getUsers(groupId);
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }

    // 유저 추가
    // 정상 작동시 등록된 유저 정보 반환
    // 유저 삭제 비정상 작동시 BAD_REQUEST
    @PostMapping("/group/admin/add-user")
    public ResponseEntity<?> addUserIntoGroup(@RequestBody Map<String, Object> requestMap){
        Group group = groupService.getGroup(mapper.convertValue(requestMap.get("group"), Integer.class));
        User user = userService.getUser(mapper.convertValue(requestMap.get("user"), Integer.class));
        int result = groupService.addUser(group, user.getUserId());

        if (result == 0) return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    // 유저 방출 (탈퇴와 구분되어있음)
    // 로그인된 유저를 포함해 최소 1명은 남아있을 수 있음으로 그룹 삭제는 이뤄지지 않음
    // 정상 작동시 탈퇴시킨 유저 정보 반환
    // 유저 삭제 비정상 작동시 BAD_REQUEST
    @PostMapping("/group/admin/remove-user")
    public ResponseEntity<?> removeUser(@RequestBody Map<String, Object> requestMap){
        Group group = groupService.getGroup(mapper.convertValue(requestMap.get("group"), Integer.class));
        User user = userService.getUser(mapper.convertValue(requestMap.get("user"), Integer.class));
        int result = groupService.removeUser(group.getId(), user.getUserId());
        if (result == 0) return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
