package CodeMaker.togetherLion.domain.post.dto;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.model.DealState;
import CodeMaker.togetherLion.domain.user.entity.User;

import java.time.LocalDateTime;

public record PostReq(
        String productName,
        String productContent,
        Integer dealNum,
        LocalDateTime deadlineDate,
        DealState dealState,
        Integer price


) {
    public Post toEntity(User user) {

        return Post.builder()
                .productName(productName)
                .productContent(productContent)
                .dealNum(dealNum)
                .deadlineDate(deadlineDate)
                .dealState(dealState)
                .price(price)
                .user(user)
                .build();

    }
}

