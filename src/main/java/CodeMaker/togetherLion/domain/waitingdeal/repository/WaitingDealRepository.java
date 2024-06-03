package CodeMaker.togetherLion.domain.waitingdeal.repository;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;





public interface WaitingDealRepository extends JpaRepository<WaitingDeal, Integer> {

    // 참여 중인 사람들
    @Query("SELECT wd.user FROM WaitingDeal wd WHERE wd.post.postId = :postId AND wd.waitingState = :waitingState ORDER BY wd.requestDate DESC")
    List<User> findUsersByPostIdAndWaitingState(@Param("postId") int postId, @Param("waitingState") WaitingState waitingState);

    @Query("SELECT wd FROM WaitingDeal wd WHERE wd.user.userId = :userId AND wd.post.postId = :postId")
    Optional<WaitingDeal> findByUserIdAndPostId(@Param("userId") int userId, @Param("postId") int postId);

    @Query("SELECT COUNT(wd) FROM WaitingDeal wd WHERE wd.post.postId = :postId AND wd.waitingState = :waitingState")
    int countByPostIdAndWaitingState(@Param("postId") int postId, @Param("waitingState") WaitingState waitingState);

    Optional<Object> findByUserUserIdAndPostPostId(int userId, int i);

    @Query("SELECT wd FROM WaitingDeal wd WHERE wd.post.postId = :postId")
    List<WaitingDeal> findWaitingDealsByPostId(@Param("postId") int postId);

    // 참여 중인 공동구매의 방장 userId
    @Query("SELECT p.user.userId FROM WaitingDeal w " +
            "INNER JOIN Post p ON w.post = p " +
            "WHERE w.post = :post")
    int findWaitingDealUserId(@Param("post") Post post);

    // 참여 중인 사람들의 userId
    @Query("SELECT wd.user.userId FROM WaitingDeal wd WHERE wd.post.postId = :postId AND wd.waitingState = :waitingState ORDER BY wd.requestDate DESC")
    List<Integer> findUserIdByPostIdAndWaitingState(@Param("postId") int postId, @Param("waitingState") WaitingState waitingState);

}



//public interface WaitingDealRepository extends JpaRepository<WaitingDeal, Integer> {
//    @Query("SELECT wd.user FROM WaitingDeal wd WHERE wd.post.postId = :postId AND wd.waitingState = :waitingState ORDER BY wd.requestDate DESC")
//    List<User> findUsersByPostIdAndWaitingState(@Param("postId") int postId, @Param("waitingState") WaitingState waitingState);
//
//
//    Optional<WaitingDeal> findByUserUserIdAndPostPostId(int userId, int postId);
//    @Query("SELECT wd FROM WaitingDeal wd WHERE wd.user.userId = :userId AND wd.post.postId = :postId")
//    Optional<WaitingDeal> findByUserIdAndPostId(@Param("userId") int userId, @Param("postId") int postId);
//
//    @Query("SELECT COUNT(wd) FROM WaitingDeal wd WHERE wd.post.postId = :postId AND wd.waitingState = :waitingState")
//    int countByPostIdAndWaitingState(int postId, WaitingState waitingState);
//
//}
