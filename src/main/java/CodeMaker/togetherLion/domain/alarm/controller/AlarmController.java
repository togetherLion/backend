package CodeMaker.togetherLion.domain.alarm.controller;

import CodeMaker.togetherLion.domain.alarm.dto.AlarmReq;
import CodeMaker.togetherLion.domain.alarm.dto.AlarmRes;
import CodeMaker.togetherLion.domain.alarm.entity.Alarm;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.alarm.service.AlarmService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final SessionUtil sessionUtil;

    // 알림 등록 - 테스트
//    @PostMapping("")
//    public ResponseEntity<AlarmRes> newAlarm(@RequestBody AlarmReq alarmReq) {
//        Alarm alarm = alarmService.newAlarm(alarmReq);
//        AlarmRes alarmRes = AlarmRes.fromEntity(alarm);
//        return ResponseEntity.ok(alarmRes);
//    }

    // 알림 목록
    @GetMapping("/list")
    public ResponseEntity<List<AlarmRes>> alarmList(HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        List<AlarmRes> alarmRes = alarmService.alarmList(userId);
        return ResponseEntity.ok(alarmRes);
    }

    // 알림 읽기
    @PostMapping("/check")
    public ResponseEntity<AlarmType> checkAlarm(@RequestParam int alarmId) {
        AlarmType alarmType = alarmService.checkAlarm(alarmId);
        return ResponseEntity.ok(alarmType);
    }
}
