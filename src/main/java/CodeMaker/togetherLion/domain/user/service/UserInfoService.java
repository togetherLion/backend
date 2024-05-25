package CodeMaker.togetherLion.domain.user.service;

import CodeMaker.togetherLion.domain.user.dto.userInfo.request.ChangeInfoReq;
import CodeMaker.togetherLion.domain.user.dto.userInfo.request.ChangePwReq;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.ChangeInfoRes;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.ChangePwRes;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;


    // 회원 정보 수정 : 지수 - 완료
    public ChangeInfoRes changeInfo(ChangeInfoReq changeInfoReq, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));

        if(!user.getName().equals(changeInfoReq.getName())) {
            user.setName(changeInfoReq.getName());
        }
        if(!user.getNickname().equals(changeInfoReq.getNickname())) {
            user.setNickname(changeInfoReq.getNickname());
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
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .userAddress(user.getUserAddress())
                .account(user.getAccount())
                .build();
    }

    // 비밀번호 변경 : 지수 - 완료
    public ChangePwRes changePw(ChangePwReq changePwReq, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));

        user.setPassword(changePwReq.getPassword());
        userRepository.save(user);

        return ChangePwRes.builder()
                .password(user.getPassword())
                .build();
    }
    
    // 회원 탈퇴 : 지수 - 완료
    public String unregister(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("잘못된 userId입니다."));
        
        user.setUserState(false);
        userRepository.save(user);

        return "unregister";
    }
}
