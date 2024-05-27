package CodeMaker.togetherLion.domain.post.service;

import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;



@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SessionUtil sessionUtil;

    public Post createPost(PostReq postReq, HttpServletRequest request) {
        // 세션에서 userId를 가져옵니다.


        int userId = sessionUtil.getUserIdFromSession(request);
        // userId를 사용하여 User 객체를 조회합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. userId=" + userId));

        // PostReq와 조회한 User 객체를 사용하여 Post 엔티티를 생성합니다.
        Post post = postReq.toEntity(user);

        // Post 엔티티를 저장합니다.
        return postRepository.save(post);
    }

    public Post getPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음!"));
        return post;
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

//    public Post updatePost(Integer postId, Post post) {
//        Post foundPost = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음!"));
//
//        foundPost.update(post.getTitle(), post.getContent());
//        return foundPost;
//    }

}
