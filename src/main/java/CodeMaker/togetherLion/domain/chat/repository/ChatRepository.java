package CodeMaker.togetherLion.domain.chat.repository;

import CodeMaker.togetherLion.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}