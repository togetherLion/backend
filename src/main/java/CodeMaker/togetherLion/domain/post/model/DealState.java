package CodeMaker.togetherLion.domain.post.model;

import lombok.Getter;

@Getter
public enum DealState {
    FIRST("모집"),
    SECOND("송금"),
    THIRD("상품배송"),
    FOURTH("상품전달"),
    FIFTH("거래완료");

    private final String message;

    DealState(String message) {
        this.message = message;
    }
}
