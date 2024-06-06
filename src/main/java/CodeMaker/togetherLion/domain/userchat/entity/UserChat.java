package CodeMaker.togetherLion.domain.userchat.entity;


import CodeMaker.togetherLion.domain.chat.entity.Chat;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.user.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Data
public class UserChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int UserPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    Chat chat;







}
