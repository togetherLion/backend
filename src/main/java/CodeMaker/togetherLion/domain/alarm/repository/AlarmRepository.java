package CodeMaker.togetherLion.domain.alarm.repository;

import CodeMaker.togetherLion.domain.alarm.entity.Alarm;
import CodeMaker.togetherLion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {

    @Query("SELECT a FROM Alarm a " +
            "WHERE a.user = :user")
    List<Alarm> findByUserId(@Param("user") User user);
}
