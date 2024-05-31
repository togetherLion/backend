package CodeMaker.togetherLion.domain.complain.dto;

import CodeMaker.togetherLion.domain.complain.entity.Complain;
import CodeMaker.togetherLion.domain.complain.model.ComplainCategory;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;

import java.time.LocalDateTime;

public record ComplainReq (

        ComplainCategory complainCategory,
        String complainContent,
        LocalDateTime complainDate,
        int targetUserId
) {

    public Complain toEntity(User targetUser, User complainUser) {

        return Complain.builder()
                .complainCategory(complainCategory)
                .complainContent(complainContent)
                .complainDate(complainDate)
                .targetUser(targetUser)
                .complainUser(complainUser)
                .build();
    }
}
