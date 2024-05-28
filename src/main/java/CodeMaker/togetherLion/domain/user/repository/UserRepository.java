package CodeMaker.togetherLion.domain.user.repository;

import CodeMaker.togetherLion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByPhone(String phone);

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    boolean existsByPhone(String phone);
}
