package CodeMaker.togetherLion.domain.cheat.repository;

import CodeMaker.togetherLion.domain.cheat.entity.Cheat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheatRepository extends JpaRepository<Cheat, Integer> {

    // 사기 계좌 테이블에 존재하는 계좌인지
    boolean existsByCheatAccount(String cheatAccount);
}
