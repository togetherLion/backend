package CodeMaker.togetherLion.domain.waitingdeal.dto;


import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;

import java.time.LocalDateTime;

public record WaitingDealReq (

        int userId,
        int postId,
        LocalDateTime requestDate,
        WaitingState waitingState
){

}