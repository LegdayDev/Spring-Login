package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String,Object> sessionStore = new ConcurrentHashMap<>(); // 동시성 문제 떄문에 ConcurrentHashMap 을 사용


    /**
     * 세션 생성
     * <li>sessionId 생성 (UUID 사용)</li>
     * <li>세션 저장소에 sessionId와 보관할 값 저장</li>
     * <li>sessionId로 응답 쿠키를 생성해서 클라이언트에 전달</li>
     */
    public void createSession(Object value, HttpServletResponse response){

        //sessionId 생성 (UUID 사용) 후 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);

        // 쿠키 생성
        response.addCookie(new Cookie(SESSION_COOKIE_NAME, sessionId));
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie == null)
            return null;

        return sessionStore.get(sessionCookie.getValue());

    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie cookie = findCookie(request, SESSION_COOKIE_NAME);
        if(cookie != null){
            sessionStore.remove(cookie.getValue());
        }
    }

    
    public Cookie findCookie(HttpServletRequest request, String cookieName){
        if(request.getCookies() == null){
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
