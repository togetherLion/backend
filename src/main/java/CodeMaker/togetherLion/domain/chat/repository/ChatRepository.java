package CodeMaker.togetherLion.domain.chat.repository;

import CodeMaker.togetherLion.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    // post_id를 통해 해당 post_id의 단일 roomId를 찾는 메소드
    @Query("SELECT c.roomId FROM Chat c WHERE c.post.postId = :postId")
    Optional<String> findRoomIdByPost_PostId(@Param("postId") int postId);

    @Query("SELECT c FROM Chat c WHERE c.roomId = :roomId")
    Chat findByRoomId(@Param("roomId") String roomId);

}