package CodeMaker.togetherLion.domain.alarm.controller;

import CodeMaker.togetherLion.domain.alarm.dto.AlarmReq;
import CodeMaker.togetherLion.domain.alarm.dto.AlarmRes;
import CodeMaker.togetherLion.domain.alarm.entity.Alarm;
import CodeMaker.togetherLion.domain.alarm.service.AlarmService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final SessionUtil sessionUtil;

    // 알림 등록 - 테스트
    @PostMapping("")
    public ResponseEntity<AlarmRes> newAlarm(@RequestBody AlarmReq alarmReq) {
        Alarm alarm = alarmService.newAlarm(alarmReq);
        AlarmRes alarmRes = AlarmRes.fromEntity(alarm);
        return ResponseEntity.ok(alarmRes);
    }


}
