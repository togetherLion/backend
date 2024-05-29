package CodeMaker.togetherLion.domain.Follow.repository;

import CodeMaker.togetherLion.domain.Follow.entity.Follow;
import CodeMaker.togetherLion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    // 팔로우 중인지 확인
    @Query("SELECT COUNT(f) > 0 FROM Follow f WHERE f.followedUser = :followedUser AND f.followingUser = :followingUser")
    boolean isFollowing(@Param("followedUser") User followedUser,
                        @Param("followingUser") User followingUser);

    // 팔로워 수 조회
    @Query("SELECT COUNT(*) FROM Follow " +
            "WHERE followedUser = :followedUser")
    int countFollower(@Param("followedUser") User followedUser);

    // 팔로잉 수 조회
    @Query("SELECT COUNT(*) FROM Follow " +
            "WHERE followingUser = :followingUser")
    int countFollowing(@Param("followingUser") User followingUser);

    // 언팔로우
    Optional<Follow> findByFollowedUserAndFollowingUser(User followedUser, User followingUser);

}
