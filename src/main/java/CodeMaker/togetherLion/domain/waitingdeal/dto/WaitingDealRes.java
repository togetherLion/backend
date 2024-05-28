package CodeMaker.togetherLion.domain.waitingdeal.dto;

import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;

import java.time.LocalDateTime;

public record WaitingDealRes (

        int waitingDealId,
        int userId,
        int postId,
        LocalDateTime requestDate,
        WaitingState waitingState
){
    public static WaitingDealRes fromEntity(WaitingDeal waitingDeal) {
        return new WaitingDealRes(waitingDeal.getWaitingDealId(), waitingDeal.getUser().getUserId(), waitingDeal.getPost().getPostId(), waitingDeal.getRequestDate(), waitingDeal.getWaitingState());
    }

}



