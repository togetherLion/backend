package CodeMaker.togetherLion.domain.review.dto;

import CodeMaker.togetherLion.domain.review.entity.Review;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;

import java.time.LocalDateTime;

public record ReviewRes (

        int reviewId,
        int userId,
        int postId,
        LocalDateTime uploadDate,
        String reviewContent,
        Integer starScore
){
    public static ReviewRes fromEntity(Review review) {
        return new ReviewRes(review.getReviewId(), review.getUser().getUserId(), review.getPost().getPostId(), review.getUploadDate(), review.getReviewContent(), review.getStarScore());
    }

}
