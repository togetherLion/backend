package CodeMaker.togetherLion.domain.complain.entity;

import CodeMaker.togetherLion.domain.complain.model.ComplainCategory;
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
public class Complain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int complainId;

    @Enumerated(EnumType.STRING)
    private ComplainCategory complainCategory;

    @Column
    private String complainContent;

    @Column
    LocalDateTime complainDate;

    @ManyToOne
    @JoinColumn(name = "targetUserId", referencedColumnName = "userId")
    private User targetUser;

    @ManyToOne
    @JoinColumn(name = "complainUserId", referencedColumnName = "userId")
    private User complainUser;

}
