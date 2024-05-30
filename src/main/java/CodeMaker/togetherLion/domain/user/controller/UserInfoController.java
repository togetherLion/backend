package CodeMaker.togetherLion.domain.user.controller;

import CodeMaker.togetherLion.domain.user.dto.userInfo.request.*;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.*;
import CodeMaker.togetherLion.domain.user.service.UserInfoService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final SessionUtil sessionUtil;

    // 회원 정보 조회
    @PostMapping("/userInfo")
    public UserInfoRes userInfo(HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.userInfo(userId);
    }


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
    public UnregisterRes unregister(@RequestBody UnregisterReq unregisterReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.unregister(unregisterReq, userId);
    }

    // 내 프로필 조회 : 지수
//    @PostMapping("/myProfile")
//    public MyProfileRes myProfile(HttpServletRequest request) {
//        int userId = sessionUtil.getUserIdFromSession(request);
//        return userInfoService.myProfile(userId);
//    }

    // 사용자 프로필 조회 : 지수 - 팔로잉, 팔로워 등 추가 필요
    @PostMapping("/userProfile")
    public UserProfileRes userProfile(@RequestBody UserProfileReq userProfileReq, HttpServletRequest request) {
        int nowUserId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.userProfile(userProfileReq, nowUserId);
    }

    @PostMapping("/modifyProfile")
    public ModifyProfileRes modifyProfile(@RequestBody ModifyProfileReq modifyProfileReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.modifyProfile(modifyProfileReq, userId);
    }


    //연관관계 매핑 - 회원 삭제, 나중에 지수랑 얘기하고 경로수정해야될듯
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable int userId){
        userInfoService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
