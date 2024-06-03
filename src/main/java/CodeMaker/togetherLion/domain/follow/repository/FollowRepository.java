package CodeMaker.togetherLion.domain.follow.repository;

import CodeMaker.togetherLion.domain.follow.entity.Follow;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.FollowerListRes;
import CodeMaker.togetherLion.domain.user.dto.userInfo.response.FollowingListRes;
import CodeMaker.togetherLion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
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

    // 팔로워 목록 조회
    @Query("SELECT new CodeMaker.togetherLion.domain.user.dto.userInfo.response.FollowerListRes(u.userId, u.nickname, u.profilePicture) " +
            "FROM Follow f " +
            "JOIN User u ON f.followingUser = u " +
            "WHERE f.followedUser = :followedUser " +
            "AND u.userState = true")
    List<FollowerListRes> getFollowerList(@Param("followedUser") User followedUser);

    // 팔로워 userId 조회
    @Query("SELECT followingUser.userId FROM Follow " +
            "WHERE followedUser = :followedUser")
    List<Integer> getFollowerIdList(@Param("followedUser") User followedUser);

    // 팔로잉 수 조회
    @Query("SELECT COUNT(*) FROM Follow f " +
            "JOIN User u ON f.followedUser = u " +
            "WHERE f.followingUser = :followingUser " +
            "AND u.userState = true")
    int countFollowing(@Param("followingUser") User followingUser);

    // 팔로잉 목록 조회
    @Query("SELECT new CodeMaker.togetherLion.domain.user.dto.userInfo.response.FollowingListRes(u.userId, u.nickname, u.profilePicture) " +
            "FROM Follow f " +
            "JOIN User u ON f.followedUser = u " +
            "WHERE f.followingUser = :followingUser " +
            "AND u.userState = true")
    List<FollowingListRes> getFollowingList(@Param("followingUser") User followingUser);

    // 언팔로우
    Optional<Follow> findByFollowedUserAndFollowingUser(User followedUser, User followingUser);

}
