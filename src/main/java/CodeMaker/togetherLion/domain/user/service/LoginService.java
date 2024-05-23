package CodeMaker.togetherLion.domain.user.service;

import CodeMaker.togetherLion.domain.util.AddrUtil;
import CodeMaker.togetherLion.domain.util.SmsUtil;
import CodeMaker.togetherLion.domain.user.dto.request.*;
import CodeMaker.togetherLion.domain.user.dto.response.*;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public SignupResponse signup(SignupRequest signupRequest) {

        User user = User.builder()
                .loginId(signupRequest.getLoginId())
                .password(signupRequest.getPassword())
                .phone(signupRequest.getPhone())
                .name(signupRequest.getName())
                .nickname(signupRequest.getNickname())
                .userAddress(addrUtil.coordToAddr(signupRequest.getUserLong(), signupRequest.getUserLat()))
                .userLat(signupRequest.getUserLat())
                .userLong(signupRequest.getUserLong())
                .complainCount(0)
                .loginCount(0)
                .userState(true)
                .build();

        userRepository.save(user);

        return new SignupResponse(user.getLoginId());
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
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByLoginId(loginRequest.getLoginId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if(!user.isUserState()) {
            throw new RuntimeException("제한된 사용자 입니다.");
        }

        if(!user.getPassword().equals(loginRequest.getPassword())) {

            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);

            if(user.getLoginCount() >= 5 && user.getLoginCount() < 10) {
                // 비밀번호 재설정
                String newPassword = generateRandomPassword(8);
                user.setPassword(newPassword);
                userRepository.save(user);

                // 메세지 전송
                smsUtil.sendOne(user.getPhone(), newPassword);

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

        return LoginResponse.builder()
                .loginId(user.getLoginId())
                .build();
    }

    // 아이디 중복 확인 : 지수 - 완료
    public IdCheckResponse idCheck(IdCheckRequest idCheckRequest) {
        String loginId = idCheckRequest.getLoginId();
        boolean isDuplicate = userRepository.existsByLoginId(loginId);

        return IdCheckResponse.builder()
                .IdCheck(isDuplicate)
                .build();
    }

    // 닉네임 중복 확인 : 지수 - 완료
    public NicknameCheckResponse nicknameCheck(NicknameCheckRequest nicknameCheckRequest) {
        String nickname = nicknameCheckRequest.getNickname();
        boolean isDuplicate = userRepository.existsByNickname(nickname);

        return NicknameCheckResponse.builder()
                .nicknameCheck(isDuplicate)
                .build();
    }

    // 전화번호 인증 : 지수 - 구현 중
    public PhoneAuthResponse phoneAuth(PhoneAuthRequest phoneAuthRequest) {
        String auth = generateRandomPassword(8);
        smsUtil.sendOne(phoneAuthRequest.getPhone(), auth); // SMSUtil 멘트 수정 필요
        
        return PhoneAuthResponse.builder()
                .phone(phoneAuthRequest.getPhone())
                .build();
    }

    // 전화번호 인증 체크 : 지수 - 개발 중 -> 임시 저장 테이블 새로 파야함
    public PhoneCheckResponse phoneCheck(PhoneCheckRequest phoneCheckRequest) {
        return null;
    }

    // 아이디 찾기 : 지수 - 완료
    public FindIdResponse findId(FindIdRequest findIdRequest) {
        User user = userRepository.findByPhone(findIdRequest.getPhone())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 전화번호입니다."));

        if(!user.getName().equals(findIdRequest.getName())) {
            throw new RuntimeException("이름과 전화번호가 일치하지 않습니다.");
        }

        return FindIdResponse.builder()
                .loginId(user.getLoginId())
                .build();
    }

    // 비밀번호 찾기 : 지수 - 완료
    public FindPwResponse findPw(FindPwRequest findPwRequest) {
        User user = userRepository.findByLoginId(findPwRequest.getLoginId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        String newPassword = generateRandomPassword(8);
        user.setPassword(newPassword);
        userRepository.save(user);

        smsUtil.sendOne(user.getPhone(), newPassword);

        return FindPwResponse.builder()
                .loginId(user.getLoginId())
                .build();
    }
}
