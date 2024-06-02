package CodeMaker.togetherLion.domain.good.controller;


import CodeMaker.togetherLion.domain.good.dto.GoodReq;
import CodeMaker.togetherLion.domain.good.dto.GoodRes;
import CodeMaker.togetherLion.domain.good.entity.Good;
import CodeMaker.togetherLion.domain.good.service.GoodService;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealReq;
import CodeMaker.togetherLion.domain.waitingdeal.dto.WaitingDealRes;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/goods")
public class GoodController {

    private final GoodService goodService;
    private final SessionUtil sessionUtil;


    @PostMapping("/postlike")
    public ResponseEntity<?> createGood(HttpServletRequest request, @RequestBody GoodReq goodReq) {
        try {
            int userId = sessionUtil.getUserIdFromSession(request); // 세션에서 userId 가져오기
            goodReq = new GoodReq(userId, goodReq.postId(), goodReq.likeCheck()); // userId 업데이트
            GoodRes goodRes = goodService.createGood(goodReq); // 서비스를 통해 Good 객체 생성 및 저장
            return ResponseEntity.ok().body(goodRes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/cancellike/{postId}")
    public String updateLikeCheckToFalse(HttpServletRequest request, @PathVariable int postId) {
        int userId = sessionUtil.getUserIdFromSession(request);
        goodService.updateLikeCheck(userId, postId);
        return "찜취소";
    }

    @PutMapping("/againlike/{postId}")
    public String againLikeCheckToFalse(HttpServletRequest request, @PathVariable int postId) {
        int userId = sessionUtil.getUserIdFromSession(request);
        goodService.againLikeCheckToFalse(userId, postId);
        return "찜하기";
    }

    @GetMapping("/liked")
    public ResponseEntity<List<PostRes>> getLikedPosts(HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        List<Post> posts = goodService.findLikedPostsByUserId(userId);
        List<PostRes> postResponses = posts.stream().map(PostRes::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(postResponses);
    }

}
