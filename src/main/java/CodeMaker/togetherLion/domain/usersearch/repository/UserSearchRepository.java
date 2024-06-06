package CodeMaker.togetherLion.domain.usersearch.repository;

import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.usersearch.entity.UserSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSearchRepository extends JpaRepository<UserSearch, Integer> {

    Optional<UserSearch> findBySearchTextAndUser(String searchText, User user);

    boolean existsBySearchTextAndUser(String searchText, User user);

    // 최근 검색어 찾기
    @Query("SELECT us.searchText FROM UserSearch us WHERE us.user = :user ORDER BY us.searchDate DESC")
    List<String> findRecentSearch(@Param("user") User user, Pageable pageable);

    // 인기 검색어 10개
    @Query("SELECT us.searchText FROM UserSearch us " +
            "GROUP BY us.searchText " +
            "ORDER BY COUNT(*) DESC, us.searchDate DESC")
    List<String> findBestSearch(Pageable pageable);


}
