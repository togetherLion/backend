package CodeMaker.togetherLion.domain.chat.cotroller;

import CodeMaker.togetherLion.domain.chat.dto.ChatRoom;
import CodeMaker.togetherLion.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatRoom createRoom(@RequestParam String name, @RequestParam Integer postId) {
        return chatService.createRoom(name, postId);
    }

    @GetMapping
    public List<ChatRoom> findAllRooms(){
        return chatService.findAllRoom();
    }

    @PostMapping("/check-and-create-room")
    public ResponseEntity<?> checkAndCreateRoom(@RequestParam int postId, @RequestParam String roomName) {

        Optional<ChatRoom> chatRoomOptional = chatService.checkAndCreateChatRoom(postId, roomName);
        if (chatRoomOptional.isPresent()) {
            return ResponseEntity.ok(chatRoomOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat room not created. Conditions not met.");
        }
    }
}
