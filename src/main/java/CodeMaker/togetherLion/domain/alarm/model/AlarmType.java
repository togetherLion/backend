package CodeMaker.togetherLion.domain.alarm.model;

import lombok.Getter;

@Getter
public enum AlarmType {

    COMPLAIN("신고 중첩"),
    NEWPOST("새 글 등록"),
    REQUEST("참여 요청"),
    REQACCEPT("공동구매 수락"),
    REQREJECT("공동구매 거절"),
    POSTMODIFY("글 수정"),
    POSTDELETE("글 삭제");

    private final String msg;

    AlarmType(String msg) {
        this.msg = msg;
    }
}
