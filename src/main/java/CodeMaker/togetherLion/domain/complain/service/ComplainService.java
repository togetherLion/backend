package CodeMaker.togetherLion.domain.complain.service;

import CodeMaker.togetherLion.domain.alarm.dto.AlarmReq;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.alarm.service.AlarmService;
import CodeMaker.togetherLion.domain.complain.dto.ComplainReq;
import CodeMaker.togetherLion.domain.complain.entity.Complain;
import CodeMaker.togetherLion.domain.complain.model.ComplainCategory;
import CodeMaker.togetherLion.domain.complain.repository.ComplainRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ComplainService {

    private final ComplainRepository complainRepository;
    private final SessionUtil sessionUtil;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    // 신고하기 - 추후에 일림 보내기 추가
    public Complain creatComplain(ComplainReq complainReq, HttpServletRequest request) {

        int userId = sessionUtil.getUserIdFromSession(request);
        User complainUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        User targetUser = userRepository.findById(complainReq.targetUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        // 저장
        Complain complain = complainReq.toEntity(targetUser, complainUser);
        complain.setComplainDate(LocalDateTime.now());
        complainRepository.save(complain);


        // 신고 당한 횟수 추가
        targetUser.setComplainCount(targetUser.getComplainCount() + 1);
        userRepository.save(targetUser);

        // 신고 당한 횟수 5번이면 경고 알림
        if(targetUser.getComplainCount() == 5) {
            // 알림 보내는 코드
            sendComplainAlarm(targetUser);
        }

        // 신고 당한 횟수가 10번 이상이면 비활성화
        if(targetUser.getComplainCount() >= 10) {
            targetUser.setUserState(false);
            userRepository.save(targetUser);
        }

        return complain;
    }

    // 신고 당한 횟수와 사유 문자열 생성
    private String complainReason(User user) {
        StringBuilder message = new StringBuilder();
        List<Object[]> lists = complainRepository.getUserComplain(user);
        boolean first = true;
        for(Object[] obj : lists) {
            if(first) {
                first = false;
            }
            else{
                message.append(", ");
            }

            ComplainCategory category = (ComplainCategory) obj[0];
            message.append(category.getMsg()).append(" ").append(obj[1]).append("건");
        }
        message.append("으로 신고당했으니 주의하세요!");

        return message.toString();
    }

    // 신고 5회 알림 전송
    private void sendComplainAlarm(User user) {
        String msg = complainReason(user);
        AlarmReq alarmReq = new AlarmReq("신고 누적 횟수 5회입니다. " + msg,
                LocalDateTime.now(), false, AlarmType.COMPLAIN, 0, user.getUserId());

        alarmService.newAlarm(alarmReq);
    }
}
