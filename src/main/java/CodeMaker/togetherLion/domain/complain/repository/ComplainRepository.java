package CodeMaker.togetherLion.domain.complain.repository;

import CodeMaker.togetherLion.domain.complain.entity.Complain;
import CodeMaker.togetherLion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplainRepository extends JpaRepository<Complain, Integer> {

    // 사용자의 신고 사유와 횟수찾기
    @Query("SELECT c.complainCategory AS category, COUNT(*) AS count FROM Complain c " +
            "WHERE c.targetUser = :targetUser " +
            "GROUP BY c.complainCategory")
    public List<Object[]> getUserComplain(@Param("targetUser")User targetUser);
}
