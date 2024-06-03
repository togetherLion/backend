package CodeMaker.togetherLion.domain.user.entity;

import CodeMaker.togetherLion.domain.alarm.entity.Alarm;
import CodeMaker.togetherLion.domain.chat.entity.Chat;
import CodeMaker.togetherLion.domain.follow.entity.Follow;
import CodeMaker.togetherLion.domain.good.entity.Good;
import CodeMaker.togetherLion.domain.post.entity.Post;
import CodeMaker.togetherLion.domain.region.entity.Region;
import CodeMaker.togetherLion.domain.review.entity.Review;
import CodeMaker.togetherLion.domain.usersearch.entity.UserSearch;
import CodeMaker.togetherLion.domain.waitingdeal.entity.WaitingDeal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column
    private String loginId;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String nickname;

    @Lob
    private String profilePicture;

    @Column
    private String profileIntro;

    @Column
    private String phone;

    @Column
    private String userAddress; // 실제 주소

    @Column
    private String userLat; // 위도

    @Column
    private String userLong; // 경도

    @Column
    private String account;

    @Column
    private int loginCount;

    @Column
    private int complainCount;

    @Column
    private boolean userState; // 비활성화 여부

    @ManyToOne
    @JoinColumn(name = "regionId") // 이는 데이터베이스의 실제 외래키 컬럼명을 지정합니다.
    private Region region;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WaitingDeal> waitingDeals = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserSearch> userSearches = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Good> good = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alarm> alarms = new ArrayList<>();

    // 사용자를 팔로우하는 관계
    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL)
    private List<Follow> followers = new ArrayList<>();

    // 사용자가 팔로우하는 관계
    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL)
    private List<Follow> following = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "chat_id")
//    private Chat chat;



}