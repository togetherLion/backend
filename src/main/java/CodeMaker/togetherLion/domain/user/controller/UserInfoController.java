package CodeMaker.togetherLion.domain.user.controller;

import CodeMaker.togetherLion.domain.user.dto.userInfo.request.ChangeInfoReq;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.ChangeInfoRes;
import CodeMaker.togetherLion.domain.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    // 회원 정보 수정 : 지수 - 완료
    @PostMapping("/change")
    public ChangeInfoRes changeInfo(@RequestBody ChangeInfoReq changeInfoReq, HttpServletRequest request) {

        HttpSession session = request.getSession(false); // 기존 세션이 있으면 반환, 없으면 null 반환

        int userId = -1;
        if (session != null) {
            userId = (int) session.getAttribute("userId");
            if (userId < 0) {
                throw new RuntimeException("UserId를 찾을 수 없음"); // userId가 세션에 없을 때의 처리
            }
        } else {
            throw new RuntimeException("세션 없음(로그인 하삼)"); // 세션이 없을 때의 처리
        }

        return userInfoService.changeInfo(changeInfoReq, userId);
    }
}
