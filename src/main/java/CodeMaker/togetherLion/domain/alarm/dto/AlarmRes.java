package CodeMaker.togetherLion.domain.alarm.dto;

import CodeMaker.togetherLion.domain.alarm.entity.Alarm;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;

import java.time.LocalDateTime;

public record AlarmRes (

    int alarmId,
    String alarmMsg,
    LocalDateTime alarmDate,
    boolean alarmCheck,
    AlarmType alarmType,
    int connectId,
    int userId

) {

    // 파라미터 여러개
    public static AlarmRes of(int alarmId, String alarmMsg, LocalDateTime alarmDate,
                              boolean alarmCheck, AlarmType alarmType, int connectId, int userId) {
        return new AlarmRes(alarmId, alarmMsg, alarmDate, alarmCheck,
                alarmType, connectId, userId);
    }

    // 단일 파라미터 (인스턴스)
    public static AlarmRes fromEntity(Alarm alarm) {
        return new AlarmRes(alarm.getAlarmId(), alarm.getAlarmMsg(), alarm.getAlarmDate(),
                alarm.isAlarmCheck(), alarm.getAlarmType(), alarm.getConnectId(), alarm.getUser().getUserId());
    }
}
