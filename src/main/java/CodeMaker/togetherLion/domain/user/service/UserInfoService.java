package CodeMaker.togetherLion.domain.user.service;

import CodeMaker.togetherLion.domain.Follow.repository.FollowRepository;
import CodeMaker.togetherLion.domain.user.dto.userInfo.request.*;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.*;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    // 회원 정보 조회
    public UserInfoRes userInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));

        return UserInfoRes.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .phone(user.getPhone())
                .account(user.getAccount())
                .userAddress(user.getUserAddress())
                .build();
    }


    // 회원 정보 수정 : 지수 - 완료
    public ChangeInfoRes changeInfo(ChangeInfoReq changeInfoReq, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));

        if(!user.getName().equals(changeInfoReq.getName())) {
            user.setName(changeInfoReq.getName());
        }
        if(!user.getPhone().equals(changeInfoReq.getPhone())) {
            user.setPhone(changeInfoReq.getPhone());
        }
        if(!user.getUserAddress().equals(changeInfoReq.getUserAddress())) {
            user.setUserAddress(changeInfoReq.getUserAddress());
        }
        if(!Objects.equals(user.getAccount(), changeInfoReq.getAccount())) {
            user.setAccount(changeInfoReq.getAccount());
        }

        userRepository.save(user);

        return ChangeInfoRes.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .userAddress(user.getUserAddress())
                .account(user.getAccount())
                .build();
    }

    // 비밀번호 변경 : 지수 - 완료
    public ChangePwRes changePw(ChangePwReq changePwReq, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));

        if(!user.getPassword().equals(changePwReq.getNowPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        if(user.getPassword().equals(changePwReq.getNewPassword())) {
            throw new RuntimeException("현재 비밀번호와 일치 합니다.");
        }

        user.setPassword(changePwReq.getNewPassword());
        userRepository.save(user);

        return ChangePwRes.builder()
                .password(user.getPassword())
                .build();
    }

    // 회원 탈퇴 : 지수 - 완료
    public UnregisterRes unregister(UnregisterReq unregisterReq, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));

        if(!unregisterReq.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        user.setUserState(false);
        userRepository.save(user);

        return UnregisterRes.builder()
                .userId(user.getUserId())
                .build();
    }

    // 내 프로필 조회 : 지수
//    public MyProfileRes myProfile(int userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));
//
//        return MyProfileRes.builder()
//                .nickname(user.getNickname())
//                .profilePicture(user.getProfilePicture())
//                .profileIntro(user.getProfileIntro())
//                .build();
//    }

    // 사용자 프로필 조회 : 지수
    public UserProfileRes userProfile(UserProfileReq userProfileReq, int nowUserId) {
        User user = userRepository.findById(userProfileReq.getUserId())
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));

        boolean isMyProfile = false;
        if(user.getUserId() == nowUserId) {
            isMyProfile = true;
        }

        User followingdUser = userRepository.findById(nowUserId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));
        boolean isFollowing = false;
        if (followRepository.isFollowing(user, followingdUser)) {
            isFollowing = true;
        }

        return UserProfileRes.builder()
                .nickname(user.getNickname())
                .profilePicture(user.getProfilePicture())
                .profileIntro(user.getProfileIntro())
                .isMyProfile(isMyProfile)
                .isFollowing(isFollowing)
                .followerCount(followRepository.countFollower(user))
                .followingCount(followRepository.countFollowing(user))
                .build();
    }

    // 프로필 수정 : 지수
    public ModifyProfileRes modifyProfile(ModifyProfileReq modifyProfileReq, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));

        if(!user.getNickname().equals(modifyProfileReq.getNickname())) {
            user.setNickname(modifyProfileReq.getNickname());
        }
        if(!user.getProfilePicture().equals(modifyProfileReq.getProfilePicture())) {
            user.setProfilePicture(modifyProfileReq.getProfilePicture());
        }
        if(!user.getProfileIntro().equals(modifyProfileReq.getProfileIntro())) {
            user.setProfileIntro(modifyProfileReq.getProfileIntro());
        }

        userRepository.save(user);

        return ModifyProfileRes.builder()
                .nickname(user.getNickname())
                .profileIntro(user.getProfileIntro())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}
