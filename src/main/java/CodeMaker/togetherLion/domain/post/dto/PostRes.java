package CodeMaker.togetherLion.domain.post.dto;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.model.DealState;

import java.time.LocalDateTime;

public record PostRes (
        int postId,
        String productName,
        String productContent,
        Integer dealNum,
        LocalDateTime deadlineDate,
        DealState dealState,
        Integer price,
        LocalDateTime uploadDate,
        int userId,
        String postPicture

){
    // 간단하게 파라미터가 여러개면? -> of, 단일 파라미터(주로 인스턴스)가 들어온다? -> from
    public static PostRes of(int postId, String productName, String productContent, Integer dealNum, LocalDateTime deadlineDate, DealState dealState, Integer price, LocalDateTime uploadDate, int userId, String postPicture ) {
        return new PostRes(postId, productName, productContent, dealNum, deadlineDate, dealState, price, uploadDate, userId, postPicture);
    }

    public static PostRes fromEntity(Post post) {
        return new PostRes(post.getPostId(), post.getProductName(), post.getProductContent(), post.getDealNum(), post.getDeadlineDate(), post.getDealState() ,post.getPrice(), post.getUploadDate(), post.getUser().getUserId(), post.getPostPicture());
    }
}
