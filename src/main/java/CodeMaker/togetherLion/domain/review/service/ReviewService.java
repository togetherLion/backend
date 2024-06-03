package CodeMaker.togetherLion.domain.review.service;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.review.dto.ReviewAlreadyExistsException;
import CodeMaker.togetherLion.domain.review.dto.ReviewReq;
import CodeMaker.togetherLion.domain.review.entity.Review;
import CodeMaker.togetherLion.domain.review.repository.ReviewRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Data
public class ReviewService {

    private final SessionUtil sessionUtil;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Review createReview(ReviewReq reviewReq, HttpServletRequest request) {


        int userId = sessionUtil.getUserIdFromSession(request);
        LocalDateTime now = LocalDateTime.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

        Post post = postRepository.findById(reviewReq.postId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

        // 기존 리뷰가 있는지 확인
        Optional<Review> existingReview = reviewRepository.findByUserIdAndPostId(userId, reviewReq.postId());
        if (existingReview.isPresent()) {
            throw new ReviewAlreadyExistsException("이미 쓴 리뷰입니다.");
        }

        Review review = Review.builder()
                .user(user)
                .post(post)
                .uploadDate(now)
                .reviewContent(reviewReq.reviewContent())
                .starScore(reviewReq.starScore())
                .build();

        return reviewRepository.save(review);

    }

    @Transactional
    public Review updateReview(int postId, ReviewReq reviewReq, HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        Review foundReview = reviewRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new NoSuchElementException("해당 리뷰가 없습니다."));
        foundReview.update(reviewReq.reviewContent(), reviewReq.starScore());

        return foundReview;

    }

    @Transactional
    public void deleteReview(Integer userId, Integer postId) {
        Review review = reviewRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new NoSuchElementException("해당 리뷰를 찾을 수 없습니다."));
        reviewRepository.delete(review);
    }



}
