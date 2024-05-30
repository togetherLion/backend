package CodeMaker.togetherLion.domain.waitingdeal.entity;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.model.DealState;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter

@Data
public class WaitingDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int waitingDealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private WaitingState waitingState; // 예: PENDING, ACCEPTED, REJECTED

    @Builder
    public WaitingDeal(User user, Post post, LocalDateTime requestDate,WaitingState waitingState ){
        this.user = user;
        this.post = post;
        this.requestDate = requestDate;
        this.waitingState = waitingState;
    }

    public void update(WaitingState waitingState){
        this.waitingState = waitingState;
    }


}
