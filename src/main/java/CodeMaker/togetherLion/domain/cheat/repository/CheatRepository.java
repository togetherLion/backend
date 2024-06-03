package CodeMaker.togetherLion.domain.cheat.repository;

import CodeMaker.togetherLion.domain.cheat.entity.Cheat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheatRepository extends JpaRepository<Cheat, Integer> {

    // 사기 계좌 테이블에 존재하는 계좌인지
    boolean existsByCheatAccount(String cheatAccount);

    // 사기 횟수 조회
    @Query("SELECT cheatCount FROM Cheat " +
            "WHERE cheatAccount = :cheatAccount")
    int getCheatAccount(@Param("cheatAccount") String cheatAccount);
}
