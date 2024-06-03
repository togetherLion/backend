package CodeMaker.togetherLion.domain.alarm.dto;

import CodeMaker.togetherLion.domain.alarm.entity.Alarm;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.user.entity.User;

import java.time.LocalDateTime;

public record AlarmReq (

    String alarmMsg,
    LocalDateTime alarmDate,
    boolean alarmCheck,
    AlarmType alarmType,
    int connectId,
    int userId
) {
    public Alarm toEntity(User user) {
        return Alarm.builder()
                .alarmMsg(alarmMsg)
                .alarmDate(alarmDate)
                .alarmCheck(alarmCheck)
                .alarmType(alarmType)
                .connectId(connectId)
                .user(user)
                .build();
    }
}
