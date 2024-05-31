package CodeMaker.togetherLion.domain.waitingdeal.controller;

import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.service.PostService;
import CodeMaker.togetherLion.domain.user.dto.waitingdeal.UserRes;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealReq;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealRes;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import CodeMaker.togetherLion.domain.waitingdeal.service.WaitingDealService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/waitingdeal")
public class WaitingDealController {

    private final WaitingDealService waitingDealService;
    private final PostService postService;


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

    @GetMapping("/useraccepted/{postId}")
    public ResponseEntity<List<UserRes>> getUsersByPostIdAndWaitingStateAccepted(@PathVariable Integer postId) {
        List<UserRes> userResponses = waitingDealService.getUsersByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);
        return ResponseEntity.ok(userResponses);
    }



}
