package CodeMaker.togetherLion.domain.user.service;

import CodeMaker.togetherLion.domain.user.dto.login.request.*;
import CodeMaker.togetherLion.domain.user.dto.login.response.*;
import CodeMaker.togetherLion.domain.util.AddrUtil;
import CodeMaker.togetherLion.domain.util.SmsUtil;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final SmsUtil smsUtil;
    private final AddrUtil addrUtil;

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom RANDOM = new SecureRandom();

    // 회원가입 : 지수 - 완료
    public SignupRes signup(SignupReq signupReq) {

        User user = User.builder()
                .loginId(signupReq.getLoginId())
                .password(signupReq.getPassword())
                .phone(signupReq.getPhone())
                .name(signupReq.getName())
                .nickname(signupReq.getNickname())
                .userAddress(signupReq.getUserAddress())
                .complainCount(0)
                .loginCount(0)
                .userState(true)
                .build();

        userRepository.save(user);

        return new SignupRes(user.getLoginId());
    }

    // 주소 찾기 : 지수
    public FindAddressRes findAddress(@NotNull FindAddressReq findAddressReq) {

        return FindAddressRes.builder()
                .userAddress(addrUtil.coordToAddr(findAddressReq.getUserLong(), findAddressReq.getUserLat()))
                .userRegion1Depth(addrUtil.coordToR1D(findAddressReq.getUserLong(), findAddressReq.getUserLat()))
                .userRegion2Depth(addrUtil.coordToR2D(findAddressReq.getUserLong(), findAddressReq.getUserLat()))
                .userRegion3Depth(addrUtil.coordToR3D(findAddressReq.getUserLong(), findAddressReq.getUserLat()))
                .build();

    }

    // 비밀번호 랜덤 생성 : 지수
    public String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }

    // 로그인 : 지수 - 완료
    public LoginRes login(LoginReq loginReq) {
        User user = userRepository.findByLoginId(loginReq.getLoginId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if(!user.isUserState()) {
            throw new RuntimeException("제한된 사용자 입니다.");
        }

        if(!user.getPassword().equals(loginReq.getPassword())) {

            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);

            if(user.getLoginCount() >= 5 && user.getLoginCount() < 10) {
                // 비밀번호 재설정
                String newPassword = generateRandomPassword(8);
                user.setPassword(newPassword);
                userRepository.save(user);

                // 메세지 전송
                smsUtil.sendPw(user.getPhone(), newPassword);

                throw new RuntimeException("랜덤 비밀번호를 전송했습니다.");
            }
            else if(user.getLoginCount() >= 10) {
                user.setUserState(false);
                userRepository.save(user);
                throw new RuntimeException("로그인 시도 횟수가 초과되었습니다.");
            }

            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        user.setLoginCount(0);
        userRepository.save(user);

        return LoginRes.builder()
                .loginId(user.getLoginId())
                .userId(user.getUserId())
                .build();
    }

    // 아이디 중복 확인 : 지수 - 완료
    public IdCheckRes idCheck(IdCheckReq idCheckReq) {
        String loginId = idCheckReq.getLoginId();
        boolean isDuplicate = userRepository.existsByLoginId(loginId);

        return IdCheckRes.builder()
                .IdCheck(isDuplicate)
                .build();
    }

    // 닉네임 중복 확인 : 지수 - 완료
    public NicknameCheckRes nicknameCheck(NicknameCheckReq nicknameCheckReq) {
        String nickname = nicknameCheckReq.getNickname();
        boolean isDuplicate = userRepository.existsByNickname(nickname);

        return NicknameCheckRes.builder()
                .nicknameCheck(isDuplicate)
                .build();
    }

    // 전화번호 인증 : 지수 - 완료
    public PhoneAuthRes phoneAuth(PhoneAuthReq phoneAuthReq) {
        String auth = generateRandomPassword(8);
        smsUtil.sendPhoneAuth(phoneAuthReq.getPhone(), auth);

        return PhoneAuthRes.builder()
                .auth(auth)
                .build();
    }


    // 아이디 찾기 : 지수 - 완료
    public FindIdRes findId(FindIdReq findIdReq) {
        User user = userRepository.findByPhone(findIdReq.getPhone())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 전화번호입니다."));

        if(!user.getName().equals(findIdReq.getName())) {
            throw new RuntimeException("이름과 전화번호가 일치하지 않습니다.");
        }

        return FindIdRes.builder()
                .loginId(user.getLoginId())
                .build();
    }

    // 비밀번호 찾기 : 지수 - 완료
    public FindPwRes findPw(FindPwReq findPwReq) {
        User user = userRepository.findByLoginId(findPwReq.getLoginId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        String newPassword = generateRandomPassword(8);
        user.setPassword(newPassword);
        userRepository.save(user);

        smsUtil.sendPw(user.getPhone(), newPassword);

        return FindPwRes.builder()
                .loginId(user.getLoginId())
                .build();
    }
}
