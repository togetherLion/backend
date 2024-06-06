package CodeMaker.togetherLion.domain.waitingdeal.service;

import CodeMaker.togetherLion.domain.alarm.dto.AlarmReq;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.alarm.service.AlarmService;
import CodeMaker.togetherLion.domain.chat.entity.Chat;
import CodeMaker.togetherLion.domain.chat.repository.ChatRepository;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.dto.waitingdeal.UserRes;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.userchat.entity.UserChat;
import CodeMaker.togetherLion.domain.userchat.repository.UserChatRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealInfo;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealReq;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealRes;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import CodeMaker.togetherLion.domain.waitingdeal.repository.WaitingDealRepository;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final ChatRepository chatRepository;
    private final AlarmService alarmService;
    private final UserChatRepository userChatRepository;

    public WaitingDeal createWaitingDeal(WaitingDealReq waitingDealReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);

        // 이미 등록된 경우 체크
        waitingDealRepository.findByUserUserIdAndPostPostId(userId, waitingDealReq.postId())
                .ifPresent(wd -> {
                    throw new IllegalStateException("이미 등록된 대기 거래입니다.");
                });

        LocalDateTime now = LocalDateTime.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

        Post post = postRepository.findById(waitingDealReq.postId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + waitingDealReq.postId()));

        WaitingDeal waitingDeal = WaitingDeal.builder()
                .user(user)
                .post(post)
                .requestDate(now)
                .waitingState(WaitingState.PENDING)
                .build();


        // 공동구매 진행자에게 참여 요청 알림 전송
        int leaderId = waitingDealRepository.findWaitingDealUserId(post);
        String msg = "[" + post.getProductName() + "] " + user.getNickname() + "님이 참여 요청을 보냈습니다.";

        AlarmReq alarmReq = new AlarmReq(msg, LocalDateTime.now(), false, AlarmType.REQUEST,
                post.getPostId(), leaderId);
        alarmService.newAlarm(alarmReq);


        return waitingDealRepository.save(waitingDeal);
    }

    public List<UserRes> getUsersByPostIdAndWaitingState(int postId, WaitingState waitingState) {
        List<User> users = waitingDealRepository.findUsersByPostIdAndWaitingState(postId, waitingState);
        return users.stream()
                .map(UserRes::fromEntity)
                .collect(Collectors.toList());
    }

    // 참여 요청 수락
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



                    // 참여 요청 수락 알림
                    String msg = "[" + post.getProductName() + "] 참여 요청이 수락되었습니다.";
                    AlarmReq alarmReq = new AlarmReq(msg, LocalDateTime.now(), false,
                            AlarmType.REQACCEPT, postId, userId);
                    alarmService.newAlarm(alarmReq);

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

    // 참여 요청 거절
    @Transactional
    public void updateWaitingDealStateToReject(int userId, int postId) {
        Optional<WaitingDeal> waitingDealOptional = waitingDealRepository.findByUserIdAndPostId(userId, postId);
        WaitingDeal waitingDeal = waitingDealOptional.get();
        waitingDeal.update(WaitingState.REJECTED);
        waitingDealRepository.save(waitingDeal);

        // 참여 요청 거절 알림
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 postId입니다."));
        String msg = "[" + post.getProductName() + "] 참여 요청이 거절되었습니다.";

        AlarmReq alarmReq = new AlarmReq(msg, LocalDateTime.now(), false,
                AlarmType.REQREJECT, postId, userId);
        alarmService.newAlarm(alarmReq);

    }

    public Map<String, Object> canCreateChatRoom(int postId, HttpServletRequest request) {
        // 현재 ACCEPTED 상태의 WaitingDeal 수를 가져옴
        int acceptedCount = waitingDealRepository.countByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);

        // Post 엔티티를 가져와 dealNum 값을 확인
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            int dealNum = post.getDealNum();

            // 조건에 따라 결과를 담을 Map 생성
            Map<String, Object> result = new HashMap<>();

            if (acceptedCount == dealNum) {
                Optional<String> roomIdOptional = chatRepository.findRoomIdByPost_PostId(postId);
                int userId = sessionUtil.getUserIdFromSession(request);

                // roomId와 userId를 Map에 저장
                //roomIdOptional.ifPresent(roomId -> result.put("roomId", roomId));


                roomIdOptional.ifPresent(roomId -> {
                    Chat chat = chatRepository.findByRoomId(roomId);
                    if (chat != null) {
                        UserChat userChat = new UserChat();
                        userChat.setChat(chat);
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
                        userChat.setUser(user);

                        userChatRepository.save(userChat);

                        result.put("chatRoomId", chat.getChatRoomId());
                        result.put("userId", userId);
                    }
                });


                return result;



            } else {
                result.put("message", "NO");
                return result;
            }
        } else {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
    }


    public List<UserRes> getAcceptedUsersByPostId(int postId) {

        Integer dealNum = postRepository.findDealNumByPostId(postId);
        int acceptedCount = waitingDealRepository.countByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);
        if (acceptedCount == dealNum) {
            return waitingDealRepository.findUsersByPostIdAndWaitingState(postId, WaitingState.ACCEPTED)
                    .stream()
                    .map(UserRes::fromEntity) // User 엔티티를 UserRes로 변환합니다.
                    .collect(Collectors.toList());
        }
        return List.of(); // 조건을 만족하지 않으면 빈 리스트 반환
    }

    public WaitingState getWaitingStateByPostIdAndUserId(int postId, int userId) {
        Optional<WaitingDeal> waitingDeal = waitingDealRepository.findByUserIdAndPostId(userId, postId);
        return waitingDeal.map(WaitingDeal::getWaitingState).orElse(null);
    }

    public WaitingDealInfo getWaitingDealInfoByPostId(int postId) {
        List<WaitingDeal> waitingDeals = waitingDealRepository.findWaitingDealsByPostId(postId);

        List<UserRes> users = waitingDeals.stream()
                .map(waitingDeal -> UserRes.fromEntity(waitingDeal.getUser()))
                .collect(Collectors.toList());

        List<WaitingDealRes> waitingStates = waitingDeals.stream()
                .map(WaitingDealRes::fromEntity)
                .collect(Collectors.toList());

        return new WaitingDealInfo(users, waitingStates);
    }

}
