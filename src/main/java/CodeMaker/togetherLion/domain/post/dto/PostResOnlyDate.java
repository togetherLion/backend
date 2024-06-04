package CodeMaker.togetherLion.domain.post.dto;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.model.DealState;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record PostResOnlyDate (
        int postId,
        String productName,
        String productContent,
        Integer dealNum,
        LocalDate deadlineDate,
        DealState dealState,
        Integer price,
        LocalDate uploadDate,
        int userId,
        String postPicture

){


}
