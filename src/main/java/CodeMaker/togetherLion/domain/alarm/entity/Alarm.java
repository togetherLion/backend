package CodeMaker.togetherLion.domain.alarm.entity;

import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Data
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int alarmId;

    @Column
    private String alarmMsg;

    @Column
    private LocalDateTime alarmDate;

    @Column
    private boolean alarmCheck;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column
    private int connectId; // postId 등

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}
