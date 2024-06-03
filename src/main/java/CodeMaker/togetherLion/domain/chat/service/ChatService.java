package CodeMaker.togetherLion.domain.chat.service;

import CodeMaker.togetherLion.domain.alarm.dto.AlarmDto;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.alarm.service.AlarmService;
import CodeMaker.togetherLion.domain.chat.dto.ChatRoom;
import CodeMaker.togetherLion.domain.chat.entity.Chat;
import CodeMaker.togetherLion.domain.chat.repository.ChatRepository;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import CodeMaker.togetherLion.domain.waitingdeal.repository.WaitingDealRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {
    private final ObjectMapper mapper;
    private Map<String, ChatRoom> chatRooms;
    private final ChatRepository chatRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final WaitingDealRepository waitingDealRepository;
    private final AlarmService alarmService;


    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom(){
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId){
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name, Integer postId) { // postId 매개변수 추가
        String roomId = UUID.randomUUID().toString(); // 랜덤한 방 아이디 생성

        ChatRoom room = ChatRoom.builder()
                .roomId(roomId)
                .name(name)
                .build();
        chatRooms.put(roomId, room);

        Post post = postRepository.findById(postId) // postId로 Post 엔티티 찾기
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

        // JPA 엔티티에 채팅방 정보 저장
        Chat chat = Chat.builder()
                .roomId(roomId)
                .chatroomName(name)
                .createChatRoom(LocalDateTime.now())
                .post(post) // Post 객체 설정
                .build();
        chatRepository.save(chat);

        // 해당 postId에서 ACCEPTED 상태인 사용자들 가져오기
//        List<User> acceptedUsers = waitingDealRepository.findUsersByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);
//
//        // 각 사용자들의 chat_id 필드 업데이트
//        for (User user : acceptedUsers) {
//            user.setChat(chat);
//            userRepository.save(user);
//        }

        return room;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    // 새로운 checkAndCreateChatRoom 메소드
    public Optional<ChatRoom> checkAndCreateChatRoom(int postId, String roomName) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            int dealNum = post.getDealNum(); // post의 dealNum 가져오기

            // 현재 ACCEPTED 상태의 WaitingDeal 수를 가져옴
            int acceptedCount = waitingDealRepository.countByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);

            // dealNum과 acceptedCount가 같으면 채팅방 생성
            if (dealNum == acceptedCount) {
                // 채팅방 생성 알림 보내기
                String msg = "[" + post.getProductName() + "] 채팅방이 생성되었습니다.";
                List<Integer> userIdList = waitingDealRepository.findUserIdByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);
                AlarmDto alarmDto = new AlarmDto(userIdList, msg, AlarmType.CREATECHAT, postId);
                alarmService.newAlarmMany(alarmDto);

                return Optional.of(createRoom(roomName, postId));
            }
        }
        return Optional.empty();
    }

    // 계좌 전송
    public String sendAccount(int userId) {
        return userRepository.getAccount(userId);
    }

}
