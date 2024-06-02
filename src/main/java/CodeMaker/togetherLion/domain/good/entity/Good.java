package CodeMaker.togetherLion.domain.good.entity;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class Good {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int goodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    Post post;


    private boolean likeCheck;

    @Builder
    public Good(User user, Post post, boolean likeCheck) {
        this.user = user;
        this.post = post;
        this.likeCheck = likeCheck;
    }



    public void update(boolean likeCheck) {
        this.likeCheck = likeCheck;
    }
}