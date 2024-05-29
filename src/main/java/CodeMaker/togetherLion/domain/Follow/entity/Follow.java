package CodeMaker.togetherLion.domain.Follow.entity;

import CodeMaker.togetherLion.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int followId;

    @ManyToOne
    @JoinColumn(name = "followedId", referencedColumnName = "userId")
    private User followedUser;

    @ManyToOne
    @JoinColumn(name = "followingId", referencedColumnName = "userId")
    private User followingUser;
}
