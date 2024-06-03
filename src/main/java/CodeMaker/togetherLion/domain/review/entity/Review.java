package CodeMaker.togetherLion.domain.review.entity;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String reviewContent;
    Integer starScore;
    LocalDateTime uploadDate;

    @Builder
    public  Review(User user, Post post, String reviewContent, int starScore, LocalDateTime uploadDate) {
        this.user = user;
        this.post = post;
        this.reviewContent = reviewContent;
        this.starScore = starScore;
        this.uploadDate = uploadDate;
    }

    public void update(String reviewContent, Integer starScore){
        if (reviewContent != null) this.reviewContent = reviewContent;
        if (starScore != null)this.starScore = starScore;
    }


}
