package CodeMaker.togetherLion.domain.post.entity;

import CodeMaker.togetherLion.domain.chat.entity.Chat;
import CodeMaker.togetherLion.domain.post.model.DealState;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Post {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<WaitingDeal> waitingDeals = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Chat> chat = new ArrayList<>();

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

    @Lob
    private String postPicture;



    @Builder
    public Post(User user, String productName, String productContent, Integer dealNum, LocalDateTime deadlineDate, DealState dealState, Integer price, LocalDateTime uploadDate, String postPicture) {
        this.user = user;
        this.productName = productName;
        this.productContent = productContent;
        this.dealNum = dealNum;
        this.deadlineDate = deadlineDate;
        this.dealState = dealState;
        this.price = price;
        this.uploadDate = uploadDate;
        this.postPicture = postPicture;

    }

    public void update(String productName, DealState dealState, String productContent, Integer dealNum, LocalDateTime deadlineDate,  Integer price, String postPicture) {
        if (productName != null) this.productName = productName;
        if (dealState != null) this.dealState = dealState;
        if (productContent != null) this.productContent = productContent;
        if (dealNum != null) this.dealNum = dealNum;
        if (deadlineDate != null) this.deadlineDate = deadlineDate;
        if (price != null) this.price = price;
        if (postPicture != null) this.postPicture = postPicture;
    }



}


