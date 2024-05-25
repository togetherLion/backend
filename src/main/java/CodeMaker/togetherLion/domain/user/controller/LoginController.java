package CodeMaker.togetherLion.domain.user.controller;

import CodeMaker.togetherLion.domain.user.dto.login.request.*;
import CodeMaker.togetherLion.domain.user.dto.login.response.*;
import CodeMaker.togetherLion.domain.user.service.LoginService;
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
public class LoginController {

    private final LoginService loginService;

    // 회원가입 : 지수 - 완료
    @PostMapping("/signup")
    public SignupRes signup(@RequestBody SignupReq signupReq) {
        return loginService.signup(signupReq);
    }

    // 주소 찾기 : 지수 - 완료
    @PostMapping("/findAddress")
    public FindAddressRes findAddress(@RequestBody FindAddressReq findAddressReq) {
        return loginService.findAddress(findAddressReq);
    }

    // 로그인 : 지수 - 완료
//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
//        return loginService.login(loginRequest);
//    }
    @PostMapping("/login")
    public LoginRes loginResponse(@RequestBody LoginReq loginReq, HttpServletRequest request) {
        LoginRes loginRes = loginService.login(loginReq);

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", loginRes.getUserId());

        return loginRes;
    }

    // 아이디 중복 확인 : 지수 - 완료
    @PostMapping("/idCheck")
    public IdCheckRes idCheck(@RequestBody IdCheckReq idCheckReq) {
        return loginService.idCheck(idCheckReq);
    }

    // 닉네임 중복 확인 : 지수 - 완료
    @PostMapping("/nicknameCheck")
    public NicknameCheckRes nicknameCheck(@RequestBody NicknameCheckReq nicknameCheckReq) {
        return loginService.nicknameCheck(nicknameCheckReq);
    }

    // 전화번호 인증 : 지수 - 완료
    @PostMapping("/phoneAuth")
    public PhoneAuthRes phoneAuth(@RequestBody PhoneAuthReq phoneAuthReq) {
        return loginService.phoneAuth(phoneAuthReq);
    }

    // 아이디 찾기 : 지수 - 완료
    @PostMapping("/findId")
    public FindIdRes findId(@RequestBody FindIdReq findIdReq) {
        return loginService.findId(findIdReq);
    }

    // 비밀번호 찾기 : 지수 - 완료
    @PostMapping("/findPw")
    public FindPwRes findPw(@RequestBody FindPwReq findPwReq) {
        return loginService.findPw(findPwReq);
    }

    // 로그아웃 : 지수 - 완료
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }

        return "logout";
    }

}
