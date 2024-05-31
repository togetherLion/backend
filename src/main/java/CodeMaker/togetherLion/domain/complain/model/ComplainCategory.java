package CodeMaker.togetherLion.domain.complain.model;

import lombok.Getter;

@Getter
public enum ComplainCategory {

    CANCEL("무단 거래 파기"),
    DISCOMFORT("불쾌감 유발"),
    NOPAY("미송금"),
    ADVERT("광고성 게시글"),
    BANSALE("거래 금지 품목 판매"),
    OTHERS("기타");

    private final String msg;

    ComplainCategory(String msg) {
        this.msg = msg;
    }
}
