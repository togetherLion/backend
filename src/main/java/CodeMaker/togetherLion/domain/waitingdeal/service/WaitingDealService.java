package CodeMaker.togetherLion.domain.waitingdeal.service;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.dto.waitingdeal.UserRes;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealReq;
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
public class WaitingDealService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SessionUtil sessionUtil;
    private final WaitingDealRepository waitingDealRepository;

    public WaitingDeal createWaitingDeal(WaitingDealReq waitingDealReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);

        // 이미 등록된 경우 체크
        waitingDealRepository.findByUserUserIdAndPostPostId(userId, waitingDealReq.postId())
                .ifPresent(wd -> {
                    throw new IllegalStateException("이미 등록된 대기 거래입니다.");
                });

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setUserId(userId);

        Post post = postRepository.findById(waitingDealReq.postId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + waitingDealReq.postId()));

        WaitingDeal waitingDeal = WaitingDeal.builder()
                .user(user)
                .post(post)
                .requestDate(now)
                .waitingState(WaitingState.PENDING)
                .build();

        return waitingDealRepository.save(waitingDeal);
    }

    public List<UserRes> getUsersByPostIdAndWaitingState(int postId, WaitingState waitingState) {
        List<User> users = waitingDealRepository.findUsersByPostIdAndWaitingState(postId, waitingState);
        return users.stream()
                .map(UserRes::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public WaitingDeal updateWaitingDeal(WaitingDealReq waitingDealReq) {
        WaitingDeal waitingDeal = waitingDealReq.toEntity();
        waitingDeal.update(WaitingState.ACCEPTED);
        return waitingDeal;
    }


}
