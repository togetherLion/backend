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
    @Query("SELECT wd.user FROM WaitingDeal wd WHERE wd.post.postId = :postId AND wd.waitingState = :waitingState ORDER BY wd.requestDate DESC")
    List<User> findUsersByPostIdAndWaitingState(@Param("postId") int postId, @Param("waitingState") WaitingState waitingState);

    Optional<WaitingDeal> findByUserUserIdAndPostPostId(int userId, int postId);


}

