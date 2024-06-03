package CodeMaker.togetherLion.domain.alarm.dto;

import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {

    List<Integer> userIdList;
    String alarmMsg;
    AlarmType alarmType;
    int connectId;
}