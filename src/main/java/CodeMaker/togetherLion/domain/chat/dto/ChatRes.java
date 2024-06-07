package CodeMaker.togetherLion.domain.chat.dto;

import CodeMaker.togetherLion.domain.chat.entity.Chat;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;

import java.time.LocalDateTime;

public record ChatRes (
        int ChatRoomId,
        String roomId,
        LocalDateTime createChatRoom,
        String chatroomName,
        int userId,
        int postId
        //PostRes postRes // 추가된 부분

){
public static ChatRes from(Chat chat) {
    return new ChatRes(chat.getChatRoomId(), chat.getRoomId(), chat.getCreateChatRoom(), chat.getChatroomName(), chat.getPost().getUser().getUserId(), chat.getPost().getPostId());
}
}



