package CodeMaker.togetherLion.domain.Follow.repository;

import CodeMaker.togetherLion.domain.Follow.entity.Follow;
import CodeMaker.togetherLion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Integer> {

    //Optional<User> findByLoginId(String loginId);

//    @Query(value = "SELECT EXISTS (" +
//            "SELECT 1 " +
//            "FROM Follow " +
//            "WHERE followedUser = :followedUser " +
//            "AND followingUser = :followingUser)")
//    boolean isFollowing(@Param("followedUser") User followedUser,
//                        @Param("followingUser") User followingUser);

    @Query("SELECT COUNT(f) > 0 FROM Follow f WHERE f.followedUser = :followedUser AND f.followingUser = :followingUser")
    boolean isFollowing(@Param("followedUser") User followedUser,
                        @Param("followingUser") User followingUser);
}
