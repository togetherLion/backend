package CodeMaker.togetherLion.domain.usersearch.repository;

import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.usersearch.entity.UserSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSearchRepository extends JpaRepository<UserSearch, Integer> {

    Optional<UserSearch> findBySearchTextAndUser(String searchText, User user);

    boolean existsBySearchTextAndUser(String searchText, User user);
}
