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
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
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
    public void updateWaitingDealStateToAccepted(int userId, int postId) {
        Optional<WaitingDeal> waitingDealOptional = waitingDealRepository.findByUserIdAndPostId(userId, postId);

        if (waitingDealOptional.isPresent()) {
            WaitingDeal waitingDeal = waitingDealOptional.get();

            // 현재 ACCEPTED 상태의 WaitingDeal 수를 가져옴
            int acceptedCount = waitingDealRepository.countByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);

            // Post 엔티티를 가져와 dealNum 값을 확인
            Optional<Post> postOptional = postRepository.findById(postId);
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                int dealNum = post.getDealNum();

                // dealNum을 초과하지 않는지 확인
                if (acceptedCount < dealNum) {
                    waitingDeal.update(WaitingState.ACCEPTED);
                    waitingDealRepository.save(waitingDeal);
                } else {
                    throw new IllegalStateException("해당 Post에 대한 WaitingDeal의 ACCEPTED 상태의 개수가 dealNum을 초과하였습니다.");
                }
            } else {
                throw new IllegalStateException("해당하는 Post를 찾을 수 없습니다.");
            }
        } else {
            throw new IllegalStateException("해당하는 WaitingDeal을 찾을 수 없습니다.");
        }
    }


}
