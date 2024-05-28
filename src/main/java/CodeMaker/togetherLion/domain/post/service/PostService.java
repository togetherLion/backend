package CodeMaker.togetherLion.domain.post.service;

import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Data
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SessionUtil sessionUtil;

    public Post createPost(Post post, HttpServletRequest request) {
        // 세션에서 userId를 가져옵니다.


        int userId = sessionUtil.getUserIdFromSession(request);
        // userId를 사용하여 User 객체를 조회합니다.
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. userId=" + userId));

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setUserId(userId);

        post.setUser(user);
        post.setUploadDate(now);

//        Post post = Post.builder()
//                .productName(postReq.productName())
//                .productContent(postReq.productContent())
//                .dealNum(postReq.dealNum())
//                .deadlineDate(postReq.deadlineDate())
//                .dealState(postReq.dealState())
//                .price(postReq.price())
//                .uploadDate(now) // 현재 시간을 uploadDate로 설정합니다.
//                .user(user)
//                .build();



        // Post 엔티티를 저장합니다.
        return postRepository.save(post);
    }

    public List<PostRes> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(PostRes::fromEntity)
                .collect(Collectors.toList());
    }

    public Post getPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음!"));
        return post;
    }

    @Transactional
    public Post updatePost(Integer postId, PostReq postReq) {
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음!"));

        foundPost.update(postReq.productName(), postReq.productContent(), postReq.dealNum(), postReq.deadlineDate(), postReq.price());
        return foundPost;
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

}
