package CodeMaker.togetherLion.domain.waitingdeal.service;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

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


}
