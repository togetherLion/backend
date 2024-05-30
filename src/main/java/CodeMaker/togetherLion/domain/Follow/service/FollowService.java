package CodeMaker.togetherLion.domain.follow.service;

import CodeMaker.togetherLion.domain.follow.dto.request.FollowReq;
import CodeMaker.togetherLion.domain.follow.dto.request.UnfollowReq;
import CodeMaker.togetherLion.domain.follow.dto.response.FollowRes;
import CodeMaker.togetherLion.domain.follow.dto.response.UnfollowRes;
import CodeMaker.togetherLion.domain.follow.entity.Follow;
import CodeMaker.togetherLion.domain.follow.repository.FollowRepository;
import CodeMaker.togetherLion.domain.user.entity.User;
import CodeMaker.togetherLion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우
    public FollowRes follow(FollowReq followReq, int followingUserId) {

        User followingUser = userRepository.findById(followingUserId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        User followedUser = userRepository.findById(followReq.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        Follow follow = Follow.builder()
                .followingUser(followingUser)
                .followedUser(followedUser)
                .build();

        followRepository.save(follow);

        return FollowRes.builder()
                .followingNickname(followingUser.getNickname())
                .followedNickname(followedUser.getNickname())
                .build();
    }

    // 언팔로우
    public UnfollowRes unfollow(UnfollowReq unfollowReq, int unfollowingUserId) {

        User unfollowingUser = userRepository.findById(unfollowingUserId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        User unfollowedUser = userRepository.findById(unfollowReq.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        Follow follow = followRepository.findByFollowedUserAndFollowingUser(unfollowedUser, unfollowingUser)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 팔로우 정보입니다."));
        followRepository.delete(follow);

        return UnfollowRes.builder()
                .unfollowingNickname(unfollowingUser.getNickname())
                .unfollowedNickname(unfollowedUser.getNickname())
                .build();
    }
}
