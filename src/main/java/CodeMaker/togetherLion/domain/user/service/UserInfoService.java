package CodeMaker.togetherLion.domain.user.service;

import CodeMaker.togetherLion.domain.follow.repository.FollowRepository;
import CodeMaker.togetherLion.domain.region.entity.Region;
import CodeMaker.togetherLion.domain.region.repository.RegionRepository;
import CodeMaker.togetherLion.domain.user.dto.userInfo.request.*;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.*;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LoginService loginService;
    private final RegionRepository regionRepository;


    // 회원 정보 조회
    public UserInfoRes userInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

        return UserInfoRes.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .phone(user.getPhone())
                .account(user.getAccount())
                .userAddress(user.getUserAddress())
                .build();
    }


    // 회원 정보 수정 : 지수 - 완료
//    public ChangeInfoRes changeInfo(ChangeInfoReq changeInfoReq, int userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));
//
//        if(!user.getName().equals(changeInfoReq.getName())) {
//            user.setName(changeInfoReq.getName());
//        }
//        if(!user.getPhone().equals(changeInfoReq.getPhone())) {
//            user.setPhone(changeInfoReq.getPhone());
//        }
//        if(!user.getUserAddress().equals(changeInfoReq.getUserAddress())) {
//            user.setUserAddress(changeInfoReq.getUserAddress());
//        }
//        if(!Objects.equals(user.getAccount(), changeInfoReq.getAccount())) {
//            user.setAccount(changeInfoReq.getAccount());
//        }
//
//        userRepository.save(user);
//
//        return ChangeInfoRes.builder()
//                .name(user.getName())
//                .phone(user.getPhone())
//                .userAddress(user.getUserAddress())
//                .account(user.getAccount())
//                .build();
//    }

    // 이름 변경
    public String changeName(HashMap<String, Object> params, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

        user.setName(params.get("name").toString());
        userRepository.save(user);

        return user.getName();
    }

    // 주소 변경
    public String changeAddr(HashMap<String, Object> params, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

        int regionId = loginService.findAddress(params.get("userLat").toString(), params.get("userLong").toString());
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 지역 id 입니다."));

        user.setUserAddress(params.get("address").toString());
        user.setUserLat(params.get("userLat").toString());
        user.setUserLong(params.get("userLong").toString());
        user.setRegion(region);
        userRepository.save(user);

        return user.getUserAddress();
    }

    // 전화번호 변경
    public String changePhone(HashMap<String, Object> params, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

        user.setPhone(params.get("phone").toString());
        userRepository.save(user);

        return user.getPhone();
    }

    // 계좌번호 변경
    public String changeAccount(HashMap<String, Object> params, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

        user.setAccount(params.get("account").toString());
        userRepository.save(user);

        return user.getAccount();
    }

    // 비밀번호 변경 : 지수 - 완료
    public ChangePwRes changePw(ChangePwReq changePwReq, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

        if(!user.getPassword().equals(changePwReq.getNowPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if(user.getPassword().equals(changePwReq.getNewPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 일치 합니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

        if(!unregisterReq.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

        boolean isMyProfile = false;
        if(user.getUserId() == nowUserId) {
            isMyProfile = true;
        }

        User followingdUser = userRepository.findById(nowUserId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));
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
                .townName(userRepository.getUserTownName(user.getUserId()))
                .build();
    }

    // 프로필 수정 : 지수
    public ModifyProfileRes modifyProfile(ModifyProfileReq modifyProfileReq, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 userId입니다."));

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

    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }
}
