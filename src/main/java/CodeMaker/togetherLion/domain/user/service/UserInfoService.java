package CodeMaker.togetherLion.domain.user.service;

import CodeMaker.togetherLion.domain.user.dto.userInfo.request.ChangeInfoReq;
import CodeMaker.togetherLion.domain.user.dto.userInfo.request.ChangePwReq;
import CodeMaker.togetherLion.domain.user.dto.userInfo.request.UnregisterReq;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.ChangeInfoRes;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.ChangePwRes;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.UnregisterRes;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.UserInfoRes;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;


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

}
