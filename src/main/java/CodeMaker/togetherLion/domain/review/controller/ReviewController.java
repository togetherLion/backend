package CodeMaker.togetherLion.domain.review.controller;

import CodeMaker.togetherLion.domain.post.dto.PostReq;
import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.review.dto.ReviewReq;
import CodeMaker.togetherLion.domain.review.dto.ReviewRes;
import CodeMaker.togetherLion.domain.review.entity.Review;
import CodeMaker.togetherLion.domain.review.service.ReviewService;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final SessionUtil sessionUtil;

    @PostMapping("")
    public ResponseEntity<ReviewRes> createReview(@RequestBody ReviewReq reviewReq, HttpServletRequest request) {
        Review createdReview = reviewService.createReview(reviewReq, request);
        ReviewRes reviewRes = ReviewRes.fromEntity(createdReview);
        return ResponseEntity.ok(reviewRes);
    }

    @Transactional
    @PutMapping("/{postId}")
    public ResponseEntity<ReviewRes> updateReview(@PathVariable Integer postId, @RequestBody ReviewReq reviewReq, HttpServletRequest request) {
        Review review = reviewService.updateReview(postId, reviewReq, request);
        ReviewRes reviewRes = ReviewRes.fromEntity(review);
        return ResponseEntity.ok(reviewRes);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer postId, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        reviewService.deleteReview(userId, postId);
        return ResponseEntity.ok().build();
    }
}
