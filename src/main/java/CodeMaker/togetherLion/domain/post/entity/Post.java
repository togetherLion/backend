package CodeMaker.togetherLion.domain.post.entity;

import CodeMaker.togetherLion.domain.post.model.DealState;
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
public class Post {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    @Column
    private String productName;

    @Column
    private String productContent;

    @Column
    private int dealNum;

    @Column
    LocalDateTime deadlineDate;

    @Enumerated(EnumType.STRING)
    private DealState dealState;

    @Column
    private int price;

    @Column
    LocalDateTime uploadDate;



}


