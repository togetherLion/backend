package CodeMaker.togetherLion.domain.cheat.service;

import CodeMaker.togetherLion.domain.cheat.repository.CheatRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Data
@Service
public class CheatService {

    private final CheatRepository cheatRepository;
    private final UserRepository userRepository;

    // 사기 전적 조회
    public String searchCheat(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        String msg;
        if(cheatRepository.existsByCheatAccount(user.getAccount())) {
            // 사기 계좌임
            int cheatCount = cheatRepository.getCheatAccount(user.getAccount());
            msg = "총 " + cheatCount + "건의 피해 사례가 조회되었습니다. 주의하세요!";
        }
        else {
            // 사기 전적 없음
            msg = "조회된 피해 사례가 없습니다.";
        }

        return msg;
    }
}
