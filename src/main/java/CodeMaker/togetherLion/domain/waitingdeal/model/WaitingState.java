package CodeMaker.togetherLion.domain.waitingdeal.model;

import lombok.Getter;

@Getter
public enum WaitingState {
    PENDING("대기중"), ACCEPTED("수락"), REJECTED("거절");

    private final String message;

    WaitingState(String message) {
        this.message = message;
    }
}
