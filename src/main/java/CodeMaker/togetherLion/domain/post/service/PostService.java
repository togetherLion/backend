package CodeMaker.togetherLion.domain.post.service;

import CodeMaker.togetherLion.domain.alarm.dto.AlarmDto;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.alarm.service.AlarmService;
import CodeMaker.togetherLion.domain.follow.repository.FollowRepository;
import CodeMaker.togetherLion.domain.post.dto.DealStateDto;
import CodeMaker.togetherLion.domain.post.dto.GetPostDto;
import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.FollowerListRes;
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
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Data
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SessionUtil sessionUtil;
    private final WaitingDealRepository waitingDealRepository;
    private final FollowRepository followRepository;
    private final AlarmService alarmService;

    public Post createPost(Post post, HttpServletRequest request) {

        int userId = sessionUtil.getUserIdFromSession(request);
        LocalDateTime now = LocalDateTime.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

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


        // 팔로워에게 새 글 알림 전송
        List<Integer> userIdList = followRepository.getFollowerIdList(user);
        String msg = user.getNickname() + "님의 새로운 글이 등록되었습니다.";

        AlarmDto alarmDto = new AlarmDto(userIdList, msg, AlarmType.NEWPOST, savedPost.getPostId());
        alarmService.newAlarmMany(alarmDto);

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

        foundPost.update(postReq.productName(), postReq.dealState(), postReq.productContent(), postReq.dealNum(), postReq.deadlineDate(), postReq.price(), postReq.postPicture());
        return foundPost;
    }

    @Transactional
    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public List<GetPostDto> getPostsByRegionAndUserId(int userId) {
        List<Post> posts = postRepository.findPostByRegion(userId);
        return posts.stream().map(post -> {
            int waitingDealsCount = waitingDealRepository.countByPostIdAndWaitingState(post.getPostId(), WaitingState.ACCEPTED);
            PostRes postRes = PostRes.fromEntity(post);
            return new GetPostDto(postRes, waitingDealsCount);
        }).collect(Collectors.toList());
    }

//    // 사용자가 속한 지역 게시글 조회 (메인 화면)
//    public List<PostRes> getRegionPosts(int userId) {
//
//        List<Post> posts = postRepository.findPostByRegion(userId);
//        return posts.stream()
//                .map(PostRes::fromEntity)
//                .collect(Collectors.toList());
//    }


    public List<PostRes> getPostsByUserId(int userId) {
        return postRepository.findPostResByUserId(userId);
    }

    public Optional<DealStateDto> getPostDealStateByPostId(int postId) {
        Optional<Post> postOptional = postRepository.findByPostId(postId);
        return postOptional.map(post -> new DealStateDto(post.getDealState(), post.getDealState().getMessage()));
    }
}
