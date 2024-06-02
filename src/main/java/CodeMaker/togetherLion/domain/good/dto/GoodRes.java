package CodeMaker.togetherLion.domain.good.dto;

import CodeMaker.togetherLion.domain.good.entity.Good;

public record GoodRes (
        int goodId,
        int userId,
        int postId,
        boolean likeCheck
){
    public static GoodRes fromEntity(Good good){
        return new GoodRes(
                good.getGoodId(), // getId()는 Good 엔티티에서 ID를 가져오는 메소드의 이름이라고 가정합니다.
                good.getUser().getUserId(),
                good.getPost().getPostId(),
                good.isLikeCheck()
        );
    }
}
