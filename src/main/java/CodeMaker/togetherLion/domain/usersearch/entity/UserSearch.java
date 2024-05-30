package CodeMaker.togetherLion.domain.usersearch.entity;

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
public class UserSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userSearchId;

    @Column
    private LocalDateTime searchDate;

    @Column
    private String searchText;

    @ManyToOne
    @JoinColumn(name = "userId") // 이는 데이터베이스의 실제 외래키 컬럼명을 지정합니다.
    private User user;
}
