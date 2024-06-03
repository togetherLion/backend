package CodeMaker.togetherLion.domain.alarm.service;

import CodeMaker.togetherLion.domain.alarm.dto.AlarmDto;
import CodeMaker.togetherLion.domain.alarm.dto.AlarmReq;
import CodeMaker.togetherLion.domain.alarm.dto.AlarmRes;
import CodeMaker.togetherLion.domain.alarm.entity.Alarm;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.alarm.repository.AlarmRepository;
import CodeMaker.togetherLion.domain.complain.service.ComplainService;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    // 알림 등록
    public Alarm newAlarm(AlarmReq alarmReq) {
        User user = userRepository.findById(alarmReq.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        Alarm alarm = alarmReq.toEntity(user);
        alarm.setAlarmDate(LocalDateTime.now());
        alarm.setAlarmCheck(false);
        alarmRepository.save(alarm);

        return alarm;
    }

    // 알림 등록 (여러 사용자)
    public void newAlarmMany(AlarmDto alarmDto) {
        for (int userId : alarmDto.getUserIdList()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

            Alarm alarm = Alarm.builder()
                    .alarmCheck(false)
                    .alarmDate(LocalDateTime.now())
                    .alarmMsg(alarmDto.getAlarmMsg())
                    .alarmType(alarmDto.getAlarmType())
                    .connectId(alarmDto.getConnectId())
                    .user(user)
                    .build();

            alarmRepository.save(alarm);
        }
    }

    // 알림 목록
    public List<AlarmRes> alarmList(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        List<Alarm> alarms = alarmRepository.findByUserId(user);

        return alarms.stream()
                .map(AlarmRes::fromEntity)
                .collect(Collectors.toList());
    }
}
