package CodeMaker.togetherLion.domain.alarm.repository;

import CodeMaker.togetherLion.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
}
