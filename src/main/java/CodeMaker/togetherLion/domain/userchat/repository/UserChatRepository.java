package CodeMaker.togetherLion.domain.userchat.repository;

import CodeMaker.togetherLion.domain.userchat.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserChatRepository extends JpaRepository<UserChat, Integer> {

    @Query("SELECT uc FROM UserChat uc WHERE uc.user.userId = :userId")
    List<UserChat> findByUserId(@Param("userId") int userId);

}