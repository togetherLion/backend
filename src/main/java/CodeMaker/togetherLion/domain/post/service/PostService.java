package CodeMaker.togetherLion.domain.post.service;

import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import CodeMaker.togetherLion.domain.waitingdeal.repository.WaitingDealRepository;
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
    private final WaitingDealRepository waitingDealRepository;

    public Post createPost(Post post, HttpServletRequest request) {

        int userId = sessionUtil.getUserIdFromSession(request);
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setUserId(userId);

        post.setUser(user);
        post.setUploadDate(now);

        Post savedPost = postRepository.save(post);

        // WaitingDeal 엔티티 생성 및 저장
        WaitingDeal waitingDeal = WaitingDeal.builder()
                .user(user)
                .post(savedPost)
                .requestDate(now)
                .waitingState(WaitingState.ACCEPTED)
                .build();

        waitingDealRepository.save(waitingDeal);

        return savedPost;
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

        foundPost.update(postReq.productName(), postReq.productContent(), postReq.dealNum(), postReq.deadlineDate(), postReq.price(), postReq.postPicture());
        return foundPost;
    }

    @Transactional
    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    // 사용자가 속한 지역 게시글 조회 (메인 화면)
    public List<PostRes> getRegionPosts(int userId) {

        List<Post> posts = postRepository.findPostByRegion(userId);
        return posts.stream()
                .map(PostRes::fromEntity)
                .collect(Collectors.toList());
    }

}
