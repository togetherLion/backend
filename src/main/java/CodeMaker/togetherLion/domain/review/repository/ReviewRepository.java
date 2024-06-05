package CodeMaker.togetherLion.domain.review.repository;

import CodeMaker.togetherLion.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.post.postId = :postId")
    Optional<Review> findByUserIdAndPostId(@Param("userId") int userId, @Param("postId") int postId);

    List<Review> findByPost_PostIdIn(List<Integer> postIds);

    @Query("SELECT ROUND(AVG(r.starScore), 1) FROM Review r " +
            "INNER JOIN Post p ON p.postId = r.post.postId " +
            "WHERE p.user.userId = :userId")
    double getAvgStarScore(@Param("userId") int userId);
}