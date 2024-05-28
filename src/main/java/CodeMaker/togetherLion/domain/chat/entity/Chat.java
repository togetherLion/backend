package CodeMaker.togetherLion.domain.chat.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ChatRoomId;
    @Column
    private String roomId;

}
