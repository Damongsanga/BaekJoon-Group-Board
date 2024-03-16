package com.ssafypjt.bboard.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    // 쿠키를 쓸 곳이 많기 때문에 상수로 만듦
    public static final String SESSION_COOKIE_NAME = "mySessionId";

    // 스프링 아이디와 객체를 맵으로 저장
    // 동시성을 위해 ConcurrentHashMap<>() 사용
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    public void createSession(Object value, HttpServletResponse response) {

        // 세션 id를 생성하고, 값을 세션에 저장
        // randomUUID() : 확실한 랜덤값을 얻을 수 있음. 자바가 제공
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        // 쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        mySessionCookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        mySessionCookie.setMaxAge(3600 * 24); // 24시간으로 쿠키 수명 설정
        response.addCookie(mySessionCookie);
    }

    public Object getSession(HttpServletRequest request) {
        // 쿠기를 찾음
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            // 만료된 쿠키를 지움
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

}