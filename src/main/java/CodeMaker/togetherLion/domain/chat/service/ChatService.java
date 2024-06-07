package CodeMaker.togetherLion.domain.chat.service;

import CodeMaker.togetherLion.domain.alarm.dto.AlarmDto;
import CodeMaker.togetherLion.domain.alarm.model.AlarmType;
import CodeMaker.togetherLion.domain.alarm.service.AlarmService;
import CodeMaker.togetherLion.domain.chat.dto.ChatRes;
import CodeMaker.togetherLion.domain.chat.dto.ChatRoom;
import CodeMaker.togetherLion.domain.chat.entity.Chat;
import CodeMaker.togetherLion.domain.chat.repository.ChatRepository;
import CodeMaker.togetherLion.domain.place.dto.PlaceDto;
import CodeMaker.togetherLion.domain.place.dto.PlaceRes;
import CodeMaker.togetherLion.domain.place.model.PlaceType;
import CodeMaker.togetherLion.domain.place.repository.PlaceRepository;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.post.repository.PostRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import CodeMaker.togetherLion.domain.userchat.entity.UserChat;
import CodeMaker.togetherLion.domain.userchat.repository.UserChatRepository;
import CodeMaker.togetherLion.domain.waitingdeal.model.WaitingState;
import CodeMaker.togetherLion.domain.waitingdeal.repository.WaitingDealRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;
import javax.persistence.Query;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final PlaceRepository placeRepository;
    private final UserChatRepository userChatRepository;

    @PersistenceContext
    private EntityManager entityManager;


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

    // 장소 추천
    public List<PlaceDto> recommendPlace(int postId) {
        List<Integer> userIdList = waitingDealRepository.findUserIdByPostIdAndWaitingState(postId, WaitingState.ACCEPTED);
        double sumLat = 0;
        double sumLong = 0;
        for(int userId : userIdList) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId입니다."));

            sumLat += Double.parseDouble(user.getUserLat());
            sumLong += Double.parseDouble(user.getUserLong());
        }

        double avgLat = sumLat / userIdList.size();
        double avgLong = sumLong / userIdList.size();

//        String sql = "SELECT place_id, place_lat, place_long, place_type, place_name, region_id, " +
//                "(6371 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(place_lat)) * COS(RADIANS(place_long) - RADIANS(:lon)) + SIN(RADIANS(:lat)) * SIN(RADIANS(place_lat)))) AS distance " +
//                "FROM togetherlion.place ORDER BY distance LIMIT 5";
//
//        Query query = entityManager.createNativeQuery(sql, "place");
//        query.setParameter("lat", avgLat);
//        query.setParameter("lon", avgLong);
//
//        List<Object[]> results = query.getResultList();
//        List<PlaceDto> dtos = new ArrayList<>();
//        for (Object[] row : results) {
//            dtos.add(new PlaceDto(
//                    (int) row[0],    // placeId
//                    (String) row[1],  // placeLat
//                    (String) row[2],  // placeLong
//                    (String) row[4],  // placeName
//                    (String) row[3]  // placeType
//            ));
//        }
//        return dtos;
        List<Object[]> results = placeRepository.findPlacesNearby(avgLat, avgLong);
        List<PlaceDto> places = new ArrayList<>();
        for (Object[] result : results) {
            places.add(new PlaceDto(
                    (int) result[0], // placeId
                    (String) result[1], // placeLat
                    (String) result[2], // placeLong
                    (String) result[4], // placeName
                    (String) result[3], // placeType
                    (Double) result[5] // distance
            ));
        }
        return places;
    }


    public List<ChatRes> findChatsByUserId(int userId) {
        // UserChat 엔티티를 통해 userId에 해당하는 chatId들을 찾음
        List<UserChat> userChats = userChatRepository.findByUserId(userId);

        // Set을 사용하여 중복된 ChatRoomId를 제거
        Set<Integer> uniqueChatRoomIds = new HashSet<>();

        // 찾은 chatId들을 이용하여 Chat 엔티티에서 동일한 ChatRoomId를 가진 Chat 객체들을 조회
        return userChats.stream()
                .map(userChat -> chatRepository.findById(userChat.getChat().getChatRoomId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(chat -> uniqueChatRoomIds.add(chat.getChatRoomId())) // 중복된 ChatRoomId 제거
                .sorted(Comparator.comparing(Chat::getCreateChatRoom).reversed()) // 최신 순으로 정렬
                .map(ChatRes::from)
                .collect(Collectors.toList());
    }
}
