package CodeMaker.togetherLion.domain.user.service;

import CodeMaker.togetherLion.domain.region.entity.Region;
import CodeMaker.togetherLion.domain.region.repository.RegionRepository;
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
    private final RegionRepository regionRepository;
    private final SmsUtil smsUtil;
    private final AddrUtil addrUtil;

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom RANDOM = new SecureRandom();

    // 회원가입 : 지수 - 완료
    public SignupRes signup(SignupReq signupReq) {

        int regionId = findAddress(signupReq.getUserLat(), signupReq.getUserLong());
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 지역 id 입니다."));

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
                .region(region)
                .build();

        userRepository.save(user);

        return new SignupRes(user.getLoginId());
    }

    public int findAddress(String Lat, String Long) {

        String city = addrUtil.coordToR1D(Long, Lat);
        String district = addrUtil.coordToR2D(Long, Lat);
        String townName = addrUtil.coordToR3D(Long, Lat);

        if(!(regionRepository.existsByCity(city)
                && regionRepository.existsByDistrict(district)
                && regionRepository.existsByTownName(townName))) {

            Region region = Region.builder()
                    .city(city)
                    .district(district)
                    .townName(townName)
                    .build();

            regionRepository.save(region);
        }

        return regionRepository.getRegionId(city, district, townName);
    }

    // 주소 찾기 : 지수
//    public FindAddressRes findAddress2(FindAddressReq findAddressReq) {
//
//        String city = addrUtil.coordToR1D(findAddressReq.getUserLong(), findAddressReq.getUserLat());
//        String district = addrUtil.coordToR2D(findAddressReq.getUserLong(), findAddressReq.getUserLat());
//        String townName = addrUtil.coordToR3D(findAddressReq.getUserLong(), findAddressReq.getUserLat());
//
//        if(!(regionRepository.existsByCity(city)
//            && regionRepository.existsByDistrict(district)
//            && regionRepository.existsByTownName(townName))) {
//
//            Region region = Region.builder()
//                    .city(city)
//                    .district(district)
//                    .townName(townName)
//                    .build();
//
//            regionRepository.save(region);
//        }
//
//        return FindAddressRes.builder()
//                //.userAddress(addrUtil.coordToAddr(findAddressReq.getUserLong(), findAddressReq.getUserLat()))
//                .userRegion1Depth(city)
//                .userRegion2Depth(district)
//                .userRegion3Depth(townName)
//                .build();
//
//    }

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

        if(userRepository.existsByPhone(phoneAuthReq.getPhone())) {
            throw new RuntimeException("이미 존재하는 전화번호 입니다.");
        }

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
