package CodeMaker.togetherLion.domain.user.controller;

import CodeMaker.togetherLion.domain.user.dto.userInfo.request.*;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.*;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.service.UserInfoService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//    @PostMapping("/changeInfo")
//    public ChangeInfoRes changeInfo(@RequestBody ChangeInfoReq changeInfoReq, HttpServletRequest request) {
//        int userId = sessionUtil.getUserIdFromSession(request);
//        return userInfoService.changeInfo(changeInfoReq, userId);
//    }

    // 회원 정보 수정 : 지수 - 완료
//    @PostMapping("/changeInfo")
//    public ChangeInfoRes changeInfo(@RequestBody ChangeInfoReq changeInfoReq, HttpServletRequest request) {
//        int userId = sessionUtil.getUserIdFromSession(request);
//        return userInfoService.changeInfo(changeInfoReq, userId);
//    }

    // 이름 수정
    @PostMapping("/changeName")
    public String changeName(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.changeName(params, userId);
    }

    // 주소 수정
    @PostMapping("/changeAddr")
    public String changeAddr(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.changeAddr(params, userId);
    }

    // 전화번호 수정
    @PostMapping("/changePhone")
    public String changePhone(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.changePhone(params, userId);
    }

    // 계좌번호 수정
    @PostMapping("/changeAccount")
    public String changeAccount(@RequestBody HashMap<String, Object> params, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.changeAccount(params, userId);
    }

    // 비밀번호 변경 : 지수 - 완료
    @PostMapping("/changePw")
    public ChangePwRes changePw(@RequestBody ChangePwReq changePwReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.changePw(changePwReq, userId);
    }

    // 회원 탈퇴 : 지수 - 완료
//    @PostMapping("/unregister")
//    public UnregisterRes unregister(@RequestBody UnregisterReq unregisterReq, HttpServletRequest request) {
//        int userId = sessionUtil.getUserIdFromSession(request);
//        return userInfoService.unregister(unregisterReq, userId);
//    }

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

    // 사용자 프로필 수정
    @PostMapping("/modifyProfile")
    public ModifyProfileRes modifyProfile(@RequestBody ModifyProfileReq modifyProfileReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return userInfoService.modifyProfile(modifyProfileReq, userId);
    }

    // 팔로워 목록 조회
    @GetMapping("/follower/{userId}")
    public ResponseEntity<List<FollowerListRes>> followerList(@PathVariable int userId) {
        List<FollowerListRes> list = userInfoService.followerList(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 팔로잉 목록 조회
    @GetMapping("/following/{userId}")
    public ResponseEntity<List<FollowingListRes>> followingList(@PathVariable int userId) {
        List<FollowingListRes> list = userInfoService.followingList(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    // 회원탈퇴
    @DeleteMapping("/unregister")
    public ResponseEntity<?> deleteUser(@RequestBody UnregisterReq unregisterReq, HttpServletRequest request){
        int userId = sessionUtil.getUserIdFromSession(request);
        userInfoService.deleteUser(unregisterReq, userId);
        return ResponseEntity.ok().build();
    }

}
