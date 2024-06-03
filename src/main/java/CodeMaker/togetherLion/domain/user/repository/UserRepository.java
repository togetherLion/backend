package CodeMaker.togetherLion.domain.user.repository;

import CodeMaker.togetherLion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByPhone(String phone);

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    boolean existsByPhone(String phone);

    @Query("SELECT r.townName FROM User AS u " +
            "INNER JOIN Region AS r ON u.region.regionId = r.regionId " +
            "WHERE u.userId = :userId")
    public String getUserTownName(@Param("userId") int userId);

    @Query("SELECT u.account FROM User As u " +
            "WHERE u.userId = :userId")
    public String getAccount(@Param("userId") int userId);
}
