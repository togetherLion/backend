package CodeMaker.togetherLion.domain.waitingdeal.dto;


import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;

import java.time.LocalDateTime;

public record WaitingDealReq (

        int userId,
        int postId,
        LocalDateTime requestDate,
        WaitingState waitingState
){

    public WaitingDeal toEntity(){

        User user = null;
        Post post = null;
        return WaitingDeal.builder()
                .user(user)
                .post(post)
                .waitingState(waitingState)
                .requestDate(requestDate)
                .build();
    }

}