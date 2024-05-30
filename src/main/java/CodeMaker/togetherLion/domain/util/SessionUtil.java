package CodeMaker.togetherLion.domain.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class SessionUtil {

    public int getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션이 있으면 반환, 없으면 null 반환

        if (session == null) {
            throw new IllegalArgumentException("세션 없음(로그인 하삼)"); // 세션이 없을 때의 처리
        }

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || userId < 0) {
            throw new IllegalArgumentException("UserId를 찾을 수 없음"); // userId가 세션에 없거나 유효하지 않을 때의 처리
        }

        return userId;
    }
}
