package CodeMaker.togetherLion.domain.user.controller;

import CodeMaker.togetherLion.domain.user.dto.userInfo.request.ChangeInfoReq;
import CodeMaker.togetherLion.domain.user.dto.userInfo.request.ChangePwReq;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.ChangeInfoRes;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.ChangePwRes;
import CodeMaker.togetherLion.domain.user.service.UserInfoService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final SessionUtil sessionUtil;

    // 회원 정보 수정 : 지수 - 완료
    @PostMapping("/changeInfo")
    public ChangeInfoRes changeInfo(@RequestBody ChangeInfoReq changeInfoReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.changeInfo(changeInfoReq, userId);
    }

    // 비밀번호 변경 : 지수 - 완료
    @PostMapping("/changePw")
    public ChangePwRes changePw(@RequestBody ChangePwReq changePwReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.changePw(changePwReq, userId);
    }

    // 회원 탈퇴 : 지수 - 완료
    @PostMapping("/unregister")
    public String unregister(HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.unregister(userId);
    }
}
