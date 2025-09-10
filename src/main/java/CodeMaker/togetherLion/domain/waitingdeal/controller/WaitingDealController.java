package CodeMaker.togetherLion.domain.waitingdeal.controller;

import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.post.service.PostService;
import CodeMaker.togetherLion.domain.user.dto.waitingdeal.UserRes;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealInfo;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealReq;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealRes;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import CodeMaker.togetherLion.domain.waitingdeal.service.WaitingDealService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/waitingdeal")
public class WaitingDealController {

    private final WaitingDealService waitingDealService;
    private final PostService postService;
    private final SessionUtil sessionUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    @PostMapping("")
    public ResponseEntity<WaitingDealRes> createWaitingDeal(@RequestBody WaitingDealReq waitingDealReq, HttpServletRequest request) {
        WaitingDeal waitingDeal = waitingDealService.createWaitingDeal(waitingDealReq, request);
        WaitingDealRes waitingDealRes = WaitingDealRes.fromEntity(waitingDeal);
        return ResponseEntity.ok(waitingDealRes);
    }

    @GetMapping("/userpending/{postId}")
    public ResponseEntity<List<UserRes>> getUsersByPostIdAndWaitingStatePending(@PathVariable Integer postId) {
        List<UserRes> userResponses = waitingDealService.getUsersByPostIdAndWaitingState(postId, WaitingState.PENDING);
        return ResponseEntity.ok(userResponses);
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptWaitingDeal(@RequestParam int userId, @RequestParam int postId) {
        try {
            waitingDealService.updateWaitingDealStateToAccepted(userId, postId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reject")
    public ResponseEntity<?> rejectWaitingDeal(@RequestParam int userId, @RequestParam int postId) {
        try {
            waitingDealService.updateWaitingDealStateToReject(userId, postId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/useraccepted/{postId}")
    public ResponseEntity<List<UserRes>> getUsersByPostIdAndWaitingStateAccepted(@PathVariable Integer postId) {
        List<UserRes> userResponses = waitingDealService.getUsersByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);
        return ResponseEntity.ok(userResponses);
    }

//    @GetMapping("/check-chat-room/{postId}")
//    public ResponseEntity<Map<String, Object>> checkChatRoomAvailability(@PathVariable int postId, HttpServletRequest request) {
//        try {
//            // 예시로, 세션에서 userId를 얻는 방법을 사용합니다.
//            int userId = sessionUtil.getUserIdFromSession(request);
//            Map<String, Object> result = waitingDealService.canCreateChatRoom(postId, request);
//
//
//            // PostRepository를 사용하여 Post 엔티티를 조회
//            Optional<Post> postOptional = postRepository.findByPostId(postId);
//            if (postOptional.isPresent()) {
//                Post post = postOptional.get();
//                // PostRes 객체를 생성하여 결과 맵에 추가
//                PostRes postRes = PostRes.fromEntity(post);
//                result.put("post", postRes);
//            } else {
//                throw new EntityNotFoundException("Post not found");
//            }
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("message", e.getMessage());
//            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//        }
//    }

    // WaitingDealController.java

    @GetMapping("/check-chat-room/{postId}")
    public ResponseEntity<Map<String, Object>> checkChatRoomAvailability(
            @PathVariable int postId, HttpServletRequest request) {
        try {
            int userId = sessionUtil.getUserIdFromSession(request); // 세션 사용자 [12]
            Map<String, Object> result = waitingDealService.canCreateChatRoom(postId, request); // roomId, userId 포함 [3]

            // Post 로드 및 PostRes 추가 (기존 로직 유지)
            Optional<Post> postOptional = postRepository.findByPostId(postId);
            if (postOptional.isEmpty()) {
                throw new EntityNotFoundException("Post not found");
            }
            PostRes postRes = PostRes.fromEntity(postOptional.get());
            result.put("post", postRes);

            // roomId가 있는 경우 사용자 정보(UserRes)와 함께 응답 포맷 구성
            if (result.containsKey("roomId")) {
                // roomId 키를 서비스에서 보장하도록 수정(아래 서비스 수정 참고) [3]
                Integer uid = (Integer) result.getOrDefault("userId", userId);
                User user = userRepository.findById(uid)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + uid));
                UserRes userRes = UserRes.fromEntity(user);

                Map<String, Object> response = new HashMap<>();
                response.put("user", userRes);
                response.put("roomId", result.get("roomId")); // UUID 문자열 [3]
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            // 조건 미충족 시 그대로 반환 (예: { "message":"NO", "post":... })
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/accepted-users")
    public List<UserRes> getAcceptedUsersByPostId(@RequestParam int postId) {
        return waitingDealService.getAcceptedUsersByPostId(postId);
    }

    @GetMapping("/my-waiting-state")
    public WaitingState getWaitingStateByPostIdAndUserId(@RequestParam int postId, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        return waitingDealService.getWaitingStateByPostIdAndUserId(postId, userId);
    }

    @GetMapping("/waiting-deals/info")
    public WaitingDealInfo getWaitingDealInfoByPostId(@RequestParam int postId) {
        return waitingDealService.getWaitingDealInfoByPostId(postId);
    }


}
