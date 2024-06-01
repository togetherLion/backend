package CodeMaker.togetherLion.domain.follow.repository;

import CodeMaker.togetherLion.domain.follow.entity.Follow;
import CodeMaker.togetherLion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    // 팔로우 중인지 확인
    @Query("SELECT COUNT(f) > 0 FROM Follow f WHERE f.followedUser = :followedUser AND f.followingUser = :followingUser")
    boolean isFollowing(@Param("followedUser") User followedUser,
                        @Param("followingUser") User followingUser);

    // 팔로워 수 조회
    @Query("SELECT COUNT(*) FROM Follow f " +
            "JOIN User u ON f.followingUser = u " +
            "WHERE f.followedUser = :followedUser " +
            "AND u.userState = true")
    int countFollower(@Param("followedUser") User followedUser);

    // 팔로잉 수 조회
    @Query("SELECT COUNT(*) FROM Follow f " +
            "JOIN User u ON f.followedUser = u " +
            "WHERE f.followingUser = :followingUser " +
            "AND u.userState = true")
    int countFollowing(@Param("followingUser") User followingUser);

    // 언팔로우
    Optional<Follow> findByFollowedUserAndFollowingUser(User followedUser, User followingUser);

}
