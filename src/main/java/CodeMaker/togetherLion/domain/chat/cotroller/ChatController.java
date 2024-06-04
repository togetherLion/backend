package CodeMaker.togetherLion.domain.chat.cotroller;

import CodeMaker.togetherLion.domain.chat.dto.ChatRoom;
import CodeMaker.togetherLion.domain.chat.service.ChatService;
import CodeMaker.togetherLion.domain.cheat.service.CheatService;
import CodeMaker.togetherLion.domain.place.dto.PlaceDto;
import CodeMaker.togetherLion.domain.place.dto.PlaceRes;
import CodeMaker.togetherLion.domain.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final SessionUtil sessionUtil;
    private final CheatService cheatService;


    //필요 없을듯.>>?
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

    // 계좌 전송 및 사기 전적
    @GetMapping("/account")
    public ResponseEntity<HashMap<String, Object>> sendAccount(HttpServletRequest request) {
        int userId = sessionUtil.getUserIdFromSession(request);
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", chatService.sendAccount(userId));
        params.put("cheatMsg", cheatService.searchCheat(userId));
        return ResponseEntity.ok(params);
    }

    // 거래 장소 추천
    @GetMapping("/recommend")
    public ResponseEntity<List<PlaceDto>> recommendPlace(@RequestParam int postId) {
        List<PlaceDto> list = chatService.recommendPlace(postId);
        return ResponseEntity.ok(list);
    }
}
