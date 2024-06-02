package CodeMaker.togetherLion.domain.good.dto;

import CodeMaker.togetherLion.domain.good.entity.Good;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.user.entity.User;

public record GoodReq (
        int userId,
        int postId,
        boolean likeCheck
){
    public Good toEntity(User user, Post post){
        return Good.builder()
                .user(user)
                .post(post)
                .likeCheck(likeCheck)
                .build();
    }
}