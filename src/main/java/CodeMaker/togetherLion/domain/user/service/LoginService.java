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
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 지역 id 입니다."));

        String picture = "data:image/png;base64,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";

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
                .profileIntro("안녕하세요~ 반갑습니다^0^")
                .profilePicture(picture)
                .region(region)
                .userLat(signupReq.getUserLat())
                .userLong(signupReq.getUserLong())
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if(!user.isUserState()) {
            new IllegalArgumentException("제한된 사용자 입니다.");
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

                throw new IllegalArgumentException("랜덤 비밀번호를 전송했습니다.");
            }
            else if(user.getLoginCount() >= 10) {
                user.setUserState(false);
                userRepository.save(user);
                throw new IllegalArgumentException("로그인 시도 횟수가 초과되었습니다.");
            }

            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
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
            throw new IllegalArgumentException("이미 존재하는 전화번호 입니다.");
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 전화번호입니다."));

        if(!user.getName().equals(findIdReq.getName())) {
            throw new IllegalArgumentException("이름과 전화번호가 일치하지 않습니다.");
        }

        return FindIdRes.builder()
                .loginId(user.getLoginId())
                .build();
    }

    // 비밀번호 찾기 : 지수 - 완료
    public FindPwRes findPw(FindPwReq findPwReq) {
        User user = userRepository.findByLoginId(findPwReq.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        String newPassword = generateRandomPassword(8);
        user.setPassword(newPassword);
        userRepository.save(user);

        smsUtil.sendPw(user.getPhone(), newPassword);

        return FindPwRes.builder()
                .loginId(user.getLoginId())
                .build();
    }
}
