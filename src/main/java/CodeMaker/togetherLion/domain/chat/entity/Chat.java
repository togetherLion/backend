package CodeMaker.togetherLion.domain.chat.entity;

import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.userchat.entity.UserChat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ChatRoomId;


    private String roomId;

    LocalDateTime createChatRoom;

    private String chatroomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<UserChat> userChats = new ArrayList<>();


    @Builder
    public Chat(String roomId, String chatroomName, LocalDateTime createChatRoom, Post post) {
        this.roomId = roomId;
        this.chatroomName = chatroomName;
        this.createChatRoom = createChatRoom;
        this.post = post;

    }


}
