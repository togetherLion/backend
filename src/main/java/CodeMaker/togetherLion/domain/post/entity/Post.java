package CodeMaker.togetherLion.domain.post.entity;

import CodeMaker.togetherLion.domain.post.model.DealState;
import CodeMaker.togetherLion.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
@DynamicUpdate
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
    private Integer dealNum;

    @Column
    LocalDateTime deadlineDate;

    @Enumerated(EnumType.STRING)
    private DealState dealState;

    @Column
    private Integer price;

    @Column
    LocalDateTime uploadDate;


    @Builder
    public Post(User user, String productName, String productContent, Integer dealNum, LocalDateTime deadlineDate, DealState dealState, Integer price, LocalDateTime uploadDate) {
        this.user = user;
        this.productName = productName;
        this.productContent = productContent;
        this.dealNum = dealNum;
        this.deadlineDate = deadlineDate;
        this.dealState = dealState;
        this.price = price;
        this.uploadDate = uploadDate;

    }

    public void update(String productName, String productContent, Integer dealNum, LocalDateTime deadlineDate,  Integer price) {
        if (productName != null) this.productName = productName;
        if (productContent != null) this.productContent = productContent;
        if (dealNum != null) this.dealNum = dealNum;
        if (deadlineDate != null) this.deadlineDate = deadlineDate;
        if (price != null) this.price = price;
    }



}


