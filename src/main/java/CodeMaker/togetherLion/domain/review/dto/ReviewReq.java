package CodeMaker.togetherLion.domain.review.dto;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.review.entity.Review;
import CodeMaker.togetherLion.domain.user.entity.User;

import java.time.LocalDateTime;

public record ReviewReq (

    int reviewId,
    int userId,
    int postId,
    LocalDateTime uploadDate,
    String reviewContent,
    Integer starScore
){
    public Review toEntity() {
        User user = null;
        Post post = null;

        return  Review.builder()
                .user(user)
                .post(post)
                .uploadDate(uploadDate)
                .reviewContent(reviewContent)
                .starScore(starScore)
                .build();

    }
}