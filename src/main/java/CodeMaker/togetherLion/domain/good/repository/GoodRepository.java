package CodeMaker.togetherLion.domain.good.repository;

import CodeMaker.togetherLion.domain.good.entity.Good;

import CodeMaker.togetherLion.domain.post.dto.PostRes;
import CodeMaker.togetherLion.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodRepository extends JpaRepository<Good, Integer> {

    @Query("SELECT g FROM Good g WHERE g.user.userId = :userId AND g.post.postId = :postId")
    Optional<Good> findByUserIdAndPostId(@Param("userId")int userId, @Param("postId")int postId);

    @Query("SELECT g.post FROM Good g WHERE g.user.userId = :userId AND g.likeCheck = true")
    List<Post> findLikedPostsByUserId(@Param("userId") int userId);
}