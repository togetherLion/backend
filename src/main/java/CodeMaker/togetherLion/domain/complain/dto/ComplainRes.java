package CodeMaker.togetherLion.domain.complain.dto;

import CodeMaker.togetherLion.domain.complain.entity.Complain;
import CodeMaker.togetherLion.domain.complain.model.ComplainCategory;

import java.time.LocalDateTime;

public record ComplainRes (

        int complainId,
        ComplainCategory complainCategory,
        String complainContent,
        LocalDateTime complainDate,
        int targetUserId,
        int complainUserId
) {

    // 파라미터 여러 개
    public static ComplainRes of(int complainId, ComplainCategory complainCategory, String complainContent,
                                 LocalDateTime complainDate, int targetUserId, int complainUserId) {
        return new ComplainRes(complainId, complainCategory, complainContent,
                complainDate, targetUserId, complainUserId);
    }

    // 단일 파라미터 (인스턴스)
    public static ComplainRes fromEntity(Complain complain) {
        return new ComplainRes(complain.getComplainId(), complain.getComplainCategory(),
                complain.getComplainContent(), complain.getComplainDate(),
                complain.getTargetUser().getUserId(), complain.getComplainUser().getUserId());
    }
}
