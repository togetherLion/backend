package CodeMaker.togetherLion.domain.complain.repository;

import CodeMaker.togetherLion.domain.complain.entity.Complain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplainRepository extends JpaRepository<Complain, Integer> {
}
