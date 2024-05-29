package CodeMaker.togetherLion.domain.Follow.service;

import CodeMaker.togetherLion.domain.Follow.dto.request.FollowReq;
import CodeMaker.togetherLion.domain.Follow.dto.response.FollowRes;
import CodeMaker.togetherLion.domain.Follow.entity.Follow;
import CodeMaker.togetherLion.domain.Follow.repository.FollowRepository;
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
}
